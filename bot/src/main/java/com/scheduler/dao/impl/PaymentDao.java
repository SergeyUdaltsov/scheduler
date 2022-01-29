package com.scheduler.dao.impl;

import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.RangeKeyCondition;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.scheduler.dao.IDynamoDbFactory;
import com.scheduler.dao.IPaymentDao;
import com.scheduler.model.Payment;
import com.scheduler.model.PaymentType;
import com.scheduler.utils.DateUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 4/15/2021
 */
public class PaymentDao extends BaseDao<Payment> implements IPaymentDao {

    public PaymentDao(IDynamoDbFactory dynamoFactory) {
        super(dynamoFactory, Payment.class);
    }

    @Override
    public List<Payment> getLastPaymentsByPlayer(String playerName, long operatorId) {
        QuerySpec querySpec = defineQuerySpec(playerName, "s > :s and oId = :oId",
                ":s", operatorId);
        return super.findAllByQuery(querySpec);
    }

    @Override
    public List<Payment> getMonthlyPaymentsByOperator(long operatorId) {
        long from = DateUtils.getStartOfCurrentMonth();
        long to = DateUtils.getEndOfCurrentMonth();
        QuerySpec querySpec = new QuerySpec()
                .withHashKey("oId", operatorId)
                .withRangeKeyCondition(new RangeKeyCondition("d").between(from, to));
        return super.findAllByIndexQueryObject(Payment.INDEX_NAME, querySpec);
    }

    @Override
    public List<Payment> getLastSundayPayments(long operatorId) {
        LocalDate sunday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        long from = sunday.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        long to = sunday.plusDays(5).atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
        QuerySpec querySpec = new QuerySpec()
                .withHashKey("oId", operatorId)
                .withRangeKeyCondition(new RangeKeyCondition("d").between(from, to))
                .withFilterExpression("t=:type and sb >:sb")
                .withProjectionExpression("n")
                .withValueMap(new ValueMap()
                        .withString(":type", PaymentType.SUNDAY_ICE.name())
                        .withNumber(":sb", 0));
        return super.findAllByIndexQueryObject(Payment.INDEX_NAME, querySpec);
    }

    @Override
    public void remove(Payment payment) {
        super.remove(payment);
    }

    @Override
    public void removeAllPayments(List<Payment> payments) {
        List<PrimaryKey> pKeys = payments.stream()
                .map(p -> new PrimaryKey("n", p.getName(), "d", p.getDate()))
                .collect(Collectors.toList());
        super.removeAll(pKeys);
    }

    @Override
    public List<Payment> getLastSundayIceBills(String playerName, long operatorId) {
        QuerySpec query = defineQuerySpec(playerName, "sb > :sb and oId = :oId",
                ":sb", operatorId);
        return super.findAllByQuery(query);
    }

    @Override
    public List<Payment> getPaymentsPortion(long from, long operatorId, boolean isPayment) {
        String conditionExpression = from == 0 ? "oId =:oId" : "oId =:oId and d < :from";
        String sumFieldName = isPayment ? Payment.SUM_FIELD : Payment.BILL_FIELD;
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression(conditionExpression)
                .withScanIndexForward(false)
                .withFilterExpression(sumFieldName + " > :sumFieldValue and h=:h")
                .withMaxResultSize(5)
                .withValueMap(new ValueMap()
                        .withNumber(":oId", operatorId)
                        .withNumber(":h", 0)
                        .withNumber(":sumFieldValue", 0));
        if (from != 0) {
            querySpec.withValueMap(new ValueMap()
                    .withNumber(":oId", operatorId)
                    .withNumber(":from", from)
                    .withNumber(":h", 0)
                    .withNumber(":sumFieldValue", 0));
        }
        return super.findAllByIndexQueryObject("oId-d-index", querySpec);
    }

    private QuerySpec defineQuerySpec(String playerName, String filterExpression, String fieldName, long operatorId) {
        long now = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();
        return new QuerySpec()
                .withKeyConditionExpression("n =:playerName and d < :now")
                .withFilterExpression(filterExpression)
                .withScanIndexForward(false)
                .withMaxResultSize(10)
                .withValueMap(new ValueMap()
                        .withNumber(fieldName, 0)
                        .withString(":playerName", playerName)
                        .withNumber(":oId", operatorId)
                        .withNumber(":now", now));
    }

    @Override
    public List<Payment> getAllPayments() {
        return super.getAllEntities();
    }

    @Override
    public List<Payment> getPaymentsByPlayer(String playerName) {
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("n =:playerName")
                .withValueMap(new ValueMap()
                        .withString(":playerName", playerName));
        return super.findAllByQuery(querySpec);
    }

    @Override
    public List<Payment> getPaymentsByOperator(long operatorId) {
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("oId = :oId")
                .withFilterExpression("s > :s")
                .withScanIndexForward(false)
                .withValueMap(new ValueMap()
                        .withNumber(":oId", operatorId)
                        .withNumber(":s", 0));
        return super.findAllByIndexQueryObject("oId-d-index", querySpec);
    }

    @Override
    public List<Payment> getAllPaymentsByOperator(long operatorId) {
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("oId = :oId")
                .withScanIndexForward(false)
                .withValueMap(new ValueMap()
                        .withNumber(":oId", operatorId));
        return super.findAllByIndexQueryObject("oId-d-index", querySpec);
    }
}
