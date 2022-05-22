package com.scheduler.localization;

import com.scheduler.model.MessageHolder;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

public interface ILocalizer {

    void localize(List<MessageHolder> holders, Update update);
}
