package com.scheduler.service;

import com.scheduler.model.Transfer;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 5/18/2021
 */
public interface ITransferService {

    void save(Transfer transfer);

    void remove(Transfer transfer);

    int getOperatorTransferSum(long operatorId);

    List<Transfer> getAllOperatorTransfers(long operatorId);

    List<Transfer> getLastTransfers(long operatorId, int limit);

    List<String> getTransferTitlesAndUpdateContextParams(Update update);

}
