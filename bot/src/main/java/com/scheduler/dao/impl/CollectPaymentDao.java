package com.scheduler.dao.impl;

import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.scheduler.dao.ICollectPaymentDao;
import com.scheduler.dao.IDynamoDbFactory;
import com.scheduler.model.Collect;
import com.scheduler.model.CollectPayment;
import com.scheduler.model.CollectType;
import com.scheduler.model.Player;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 1/4/2022
 */
public class CollectPaymentDao extends BaseDao<CollectPayment> implements ICollectPaymentDao {

    public CollectPaymentDao(IDynamoDbFactory dynamoFactory) {
        super(dynamoFactory, CollectPayment.class);
    }

    @Override
    public void createCollectBills(List<Player> players, Collect collect, long operatorId) {
        List<CollectPayment> collectBills = players.stream().map(p -> CollectPayment.builder()
                .withBill(collect.getSum())
                .withName(collect.getName())
                .withOperator(operatorId)
                .withPlayer(p.getName())
                .withPayed(false)
                .build())
                .collect(Collectors.toList());
        saveAllItems(collectBills);
    }

    @Override
    public void deleteCollectPaymentsByCollectName(String collectName, long operatorId) {
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("n =:collectName")
                .withFilterExpression("oId =:operatorId")
                .withScanIndexForward(false)
                .withValueMap(new ValueMap()
                        .withString(":collectName", collectName)
                        .withNumber(":operatorId", operatorId));
        List<CollectPayment> allCollectPayments = findAllByQuery(querySpec);
        List<PrimaryKey> pKeys = allCollectPayments.stream()
                .map(cp -> new PrimaryKey(CollectPayment.HASH_KEY_FIELD, cp.getName(),
                        CollectPayment.RANGE_KEY_FIELD, cp.getPlayer()))
                .collect(Collectors.toList());
        removeAll(pKeys);
    }

    @Override
    public void createCollectPayment(Collect collect, Update update, String playerName, CollectType collectType) {
        String collectName = collect.getName();
        AttributeUpdate payedUpdate = new AttributeUpdate(CollectPayment.PAYED_FIELD).put(true);
        AttributeUpdate typeUpdate = new AttributeUpdate(CollectPayment.TYPE_FIELD).put(collectType.getTitle());
        updateItem(new PrimaryKey(CollectPayment.HASH_KEY_FIELD, collectName,
                CollectPayment.RANGE_KEY_FIELD, playerName), payedUpdate, typeUpdate);
    }

    @Override
    public List<CollectPayment> listAllCollects(long operatorId) {
        ScanSpec scanSpec = new ScanSpec()
                .withFilterExpression("oId =:operator")
                .withValueMap(new ValueMap().withNumber(":operator", operatorId));
        return findAll(scanSpec);
    }

    @Override
    public List<CollectPayment> listCollectPayments(String collectName, long operatorId, boolean payed) {
        QuerySpec querySpec = new QuerySpec().withHashKey(CollectPayment.HASH_KEY_FIELD, collectName)
                .withFilterExpression("oId =:operator AND payed = :payed")
                .withValueMap(new ValueMap()
                        .withNumber(":operator", operatorId)
                        .withBoolean(":payed", payed));
        return findAllByQuery(querySpec);
    }

    @Override
    public List<CollectPayment> collectPaymentsByName(String collectName, long operatorId) {
        QuerySpec querySpec = new QuerySpec().withHashKey(CollectPayment.HASH_KEY_FIELD, collectName)
                .withFilterExpression("oId =:operator")
                .withValueMap(new ValueMap()
                        .withNumber(":operator", operatorId));
        return findAllByQuery(querySpec);
    }

    @Override
    public void deleteCollectPayment(String playerName, String collectName) {
        remove(new CollectPayment(collectName, playerName));
    }
}
