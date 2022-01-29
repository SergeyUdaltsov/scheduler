package com.scheduler.helper.impl.transfer;

import com.scheduler.helper.ITransferHelper;
import com.scheduler.model.CommandType;
import com.scheduler.model.MessageHolder;
import com.scheduler.model.PaymentType;
import com.scheduler.utils.MessageUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Serhii_Udaltsov on 6/2/2021
 */
public class TransferCreateHelper implements ITransferHelper {

    @Override
    public MessageHolder getMessage(Update update) {
        List<String> titles = PaymentType.uiTypes().stream()
                .map(PaymentType::getValue)
                .collect(Collectors.toList());
        return MessageUtils.commonUnCheckableVerticalHolder(titles, "Выбери тип перевода");
    }

    @Override
    public CommandType getNextCommandType() {
        return CommandType.TRANSFER_BUILD;
    }
}
