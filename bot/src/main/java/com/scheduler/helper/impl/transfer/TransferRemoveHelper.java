package com.scheduler.helper.impl.transfer;

import com.scheduler.helper.ITransferHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.service.ITransferService;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * @author Serhii_Udaltsov on 6/2/2021
 */
public class TransferRemoveHelper implements ITransferHelper {

    private ITransferService transferService;

    public TransferRemoveHelper(ITransferService transferService) {
        this.transferService = transferService;
    }

    @Override
    public MessageHolder getMessage(Update update) {
        List<String> titles = transferService.getTransferTitlesAndUpdateContextParams(update);
        return MessageUtils.commonUnCheckableVerticalHolder(titles, "Выбери перевод");
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.TRANSFER_REMOVE;
    }
}
