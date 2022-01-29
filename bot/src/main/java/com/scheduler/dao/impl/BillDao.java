package com.scheduler.dao.impl;

import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.ScanSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.scheduler.dao.IBillDao;
import com.scheduler.dao.IDynamoDbFactory;
import com.scheduler.model.Bill;
import com.scheduler.model.PaymentType;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Serhii_Udaltsov on 5/4/2021
 */
public class BillDao extends BaseDao<Bill> implements IBillDao {

    public BillDao(IDynamoDbFactory dynamoFactory) {
        super(dynamoFactory, Bill.class);
    }

    @Override
    public List<Bill> getLastBills(PaymentType type) {
        QuerySpec querySpec = new QuerySpec()
                .withHashKey("t", type.name())
                .withMaxResultSize(3)
                .withScanIndexForward(false);
        return super.findAllByQuery(querySpec);
    }

    @Override
    public List<Bill> getAllBills() {
        return super.getAllEntities();
    }

    @Override
    public List<Bill> getMonthlyBills() {
        int month = LocalDate.now().getMonth().getValue();
        ScanSpec scanSpec = new ScanSpec()
                .withFilterExpression("m=:m")
                .withValueMap(new ValueMap()
                        .withNumber(":m", month));
        return super.findAll(scanSpec);
    }
}
