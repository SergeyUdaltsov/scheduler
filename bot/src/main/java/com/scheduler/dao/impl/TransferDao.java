package com.scheduler.dao.impl;

import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.scheduler.dao.IDynamoDbFactory;
import com.scheduler.dao.ITransferDao;
import com.scheduler.model.Transfer;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 5/18/2021
 */
public class TransferDao extends BaseDao<Transfer> implements ITransferDao {

    public TransferDao(IDynamoDbFactory dynamoFactory) {
        super(dynamoFactory, Transfer.class);
    }

    @Override
    public List<Transfer> getAllOperatorTransfers(long operatorId) {
        return getLastOperatorTransfers(operatorId, 0);
    }

    @Override
    public List<Transfer> getLastOperatorTransfers(long operatorId, int limit) {
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("oId = :oId")
                .withScanIndexForward(false)
                .withValueMap(new ValueMap()
                        .withNumber(":oId", operatorId));
        if (limit != 0) {
            querySpec.withMaxResultSize(limit);
        }
        return super.findAllByQuery(querySpec);
    }
}
