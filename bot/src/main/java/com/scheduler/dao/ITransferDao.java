package com.scheduler.dao;

import com.scheduler.model.Transfer;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 5/18/2021
 */
public interface ITransferDao {

    void save(Transfer transfer);

    void remove(Transfer transfer);

    List<Transfer> getAllOperatorTransfers(long operatorId);

    List<Transfer> getLastOperatorTransfers(long operatorId, int limit);
}
