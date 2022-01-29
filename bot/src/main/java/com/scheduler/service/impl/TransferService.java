package com.scheduler.service.impl;

import com.scheduler.dao.ITransferDao;
import com.scheduler.model.Transfer;
import com.scheduler.service.IContextService;
import com.scheduler.service.ITransferService;
import com.scheduler.utils.CollectionUtils;
import com.scheduler.utils.DateUtils;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 5/18/2021
 */
public class TransferService implements ITransferService {

    private ITransferDao transferDao;
    private IContextService contextService;

    public TransferService(ITransferDao transferDao, IContextService contextService) {
        this.transferDao = transferDao;
        this.contextService = contextService;
    }

    @Override
    public void save(Transfer transfer) {
        transferDao.save(transfer);
    }

    @Override
    public int getOperatorTransferSum(long operatorId) {
        List<Transfer> allTransfers = getAllOperatorTransfers(operatorId);
        return allTransfers.stream().mapToInt(Transfer::getSum).sum();
    }

    @Override
    public void remove(Transfer transfer) {
        transferDao.remove(transfer);
    }

    @Override
    public List<Transfer> getAllOperatorTransfers(long operatorId) {
        return transferDao.getAllOperatorTransfers(operatorId);
    }

    @Override
    public List<Transfer> getLastTransfers(long operatorId, int limit) {
        return transferDao.getLastOperatorTransfers(operatorId, limit);
    }

    @Override
    public List<String> getTransferTitlesAndUpdateContextParams(Update update) {
        long userId = MessageUtils.getUserIdFromUpdate(update);
        List<Transfer> transfers = getLastTransfers(userId, 5);
        if (CollectionUtils.isEmpty(transfers)) {
            return Collections.emptyList();
        }
        Map<String, Transfer> transferMap = transfers.stream()
                .collect(Collectors.toMap(tr -> String.format("%s Сумма: %s, %s", DateUtils.convertToUserDate(tr.getDate()),
                        tr.getSum(), tr.getType().getValue()), Function.identity()));
        Map<String, Object> params = CollectionUtils.<String, Object>mapBuilder()
                .withPair("transfers", transferMap)
                .build();
        contextService.updateContextParams(update, params);
        return CollectionUtils.newList(transferMap.keySet());
    }
}
