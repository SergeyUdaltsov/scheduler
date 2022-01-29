package com.scheduler.dao.impl;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.KeyAttribute;
import com.amazonaws.services.dynamodbv2.document.Page;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.BatchWriteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.scheduler.dao.IBaseDao;
import com.scheduler.dao.IDynamoDbFactory;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.JsonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 4/10/2021
 */
public abstract class BaseDao<ENTITY> implements IBaseDao<ENTITY> {
    private IDynamoDbFactory dynamoFactory;
    private String tableName;
    private Class<ENTITY> entityClass;
    private DynamoDBMapper dynamoDBMapper;
    private DynamoDB dynamoDB;

    public BaseDao() {
    }

    public BaseDao(IDynamoDbFactory dynamoFactory, Class<ENTITY> entityClass) {
        this.dynamoFactory = dynamoFactory;
        this.entityClass = entityClass;
        DynamoDBTable annotation = (DynamoDBTable) entityClass.getAnnotation(DynamoDBTable.class);
        if (annotation == null) {
            throw new RuntimeException("@DynamoDbTable name cannot be null.");
        } else {
            String tableName = annotation.tableName();
            if (tableName.equals("")) {
                throw new RuntimeException("Table name should not be null.");
            } else {
                this.tableName = tableName;
            }
        }
    }

    @Override
    public void updateItem(PrimaryKey primaryKey, AttributeUpdate... attributeUpdates) {
        getTable().updateItem(primaryKey, attributeUpdates);
    }

    @Override
    public List<Item> getItemsByIndex(String indexName, String hashKeyName, String hashKeyValue) {
        Table table = getTable();
        Index index = table.getIndex(indexName);
        ItemCollection<QueryOutcome> results = index.query(hashKeyName, hashKeyValue);
        Iterator<Item> iterator = results.iterator();
        List<Item> resultItems = new ArrayList<>();
        while (iterator.hasNext()) {
            resultItems.add(iterator.next());
        }
        return resultItems;
    }

    @Override
    public List<ENTITY> findAllByKeyAttribute(KeyAttribute keyAttribute) {
        ItemCollection<QueryOutcome> queryOutcome = getTable().query(keyAttribute);
        return extractEntitiesFromQueryOutcome(queryOutcome);
    }

    @Override
    public List<ENTITY> findAllByQuery(QuerySpec querySpec) {
        ItemCollection<QueryOutcome> queryOutcome = getTable().query(querySpec);
        List<Item> items = getItemsFromQueryResult(queryOutcome);
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }

        return items.stream()
                .map(this::extractEntityFromItemJson)
                .collect(Collectors.toList());
    }

    @Override
    public void remove(ENTITY item) {
        getMapper().delete(item);
    }

    @Override
    public void removeAll(List<PrimaryKey> primaryKeys) {
        TableWriteItems tableWriteItems = new TableWriteItems(tableName);
        List<List<PrimaryKey>> chunkedKeys = CollectionUtils.chunked(primaryKeys, 24);
        for (List<PrimaryKey> keys : chunkedKeys) {
            if (CollectionUtils.isEmpty(keys)) {
                continue;
            }
            tableWriteItems.withPrimaryKeysToDelete(keys.toArray(new PrimaryKey[0]));
            BatchWriteItemSpec batchWriteItemSpec = new BatchWriteItemSpec()
                    .withTableWriteItems(tableWriteItems);
            getDynamoDB().batchWriteItem(batchWriteItemSpec);
        }
    }

    @Override
    public void saveAll(List<Item> items) {
        TableWriteItems tableWriteItems = new TableWriteItems(tableName);
        tableWriteItems.withItemsToPut(items);
        getDynamoDB().batchWriteItem(tableWriteItems);
    }

    @Override
    public void saveAllItems(List<ENTITY> items) {
        getMapper().batchSave(items);
    }

    public void save(ENTITY item, DynamoDBMapperConfig config) {
        getMapper().save(item, config);
    }

    public void save(ENTITY item) {
        getMapper().save(item);
    }

    public List<ENTITY> getAllEntities() {
        return getMapper().scan(this.entityClass, new DynamoDBScanExpression());
    }

    public List<ENTITY> findAll(ScanSpec scanSpec) {
        ItemCollection<ScanOutcome> scanResult = getTable().scan(scanSpec);
        return extractEntitiesFromScan(scanResult);
    }

    public ENTITY getEntityByPrimaryKey(PrimaryKey key) {
        Item item = getTable().getItem(key);
        if (item == null) {
            return null;
        }
        return extractEntityFromItemJson(item);
    }

    @Override
    public ENTITY getEntityByScanSpec(ScanSpec scanSpec) {
        ItemCollection<ScanOutcome> scanResult = getTable().scan(scanSpec);
        List<ENTITY> entities = extractEntitiesFromScan(scanResult);
        return entities.stream().findFirst().orElse(null);
    }

    public ENTITY getEntityByQueryObject(ENTITY object) {
        return getMapper().load(object);
    }

    @Override
    public List<ENTITY> findAllByIndexQueryObject(String indexName, QuerySpec querySpec) {
        ItemCollection<QueryOutcome> result = getTable().getIndex(indexName).query(querySpec);
        List<Item> items = getItemsFromQueryResult(result);
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptyList();
        }

        return items.stream()
                .map(this::extractEntityFromItemJson)
                .collect(Collectors.toList());
    }

    public DynamoDBMapper getMapper() {
        if (dynamoDBMapper == null) {
            dynamoDBMapper = dynamoFactory.buildMapper();
        }
        return dynamoDBMapper;
    }

    public DynamoDB getDynamoDB() {
        if (dynamoDB == null) {
            dynamoDB = dynamoFactory.buildDynamo();
        }
        return dynamoDB;
    }

    private Table getTable() {
        return getDynamoDB().getTable(tableName);
    }

    private List<ENTITY> extractEntitiesFromQueryOutcome(ItemCollection<QueryOutcome> items) {
        List<ENTITY> result = new ArrayList<>();
        for (Item item : items) {
            result.add(extractEntityFromItemJson(item));
        }
        return result;
    }

    private ENTITY extractEntityFromItemJson(Item item) {
        try {
            return JsonUtils.getObjectFromJsonString(entityClass, item.toJSON());
        } catch (Exception e) {
            throw new RuntimeException("Cannot parse json", e);
        }
    }

    private List<ENTITY> extractEntitiesFromScan(ItemCollection<ScanOutcome> items) {
        List<ENTITY> result = new ArrayList<>();
        for (Item item : items) {
            result.add(extractEntityFromItemJson(item));
        }
        return result;
    }

    private List<Item> getItemsFromQueryResult(ItemCollection<QueryOutcome> queryOutcome) {
        List<Item> queryResult = new ArrayList<>();
        for (Page<Item, QueryOutcome> page : queryOutcome.pages()) {
            for (Item item : page) {
                queryResult.add(item);
            }
        }
        return queryResult;
    }
}
