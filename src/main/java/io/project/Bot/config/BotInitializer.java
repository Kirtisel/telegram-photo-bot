package io.project.Bot.config;

import io.project.Bot.service.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import io.project.Bot.config.BotConfig;

@Component
public class BotInitializer {
    @Autowired
    TelegramBot bot;
    @Autowired
    BotConfig config;

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException{
        try{
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        SetWebhook setWebhook = new SetWebhook();
        System.out.println("url  " + config.getPhotoPath());
        setWebhook.setUrl(config.getUrl()); // записать url

            telegramBotsApi.registerBot(bot, setWebhook);

            System.out.println("Бот успешно зарегистрирован. Вебхук установлен на: {}" + config.getUrl());
        }

        catch (TelegramApiException e){
            System.out.println("ошибка");
        }
    }
}
