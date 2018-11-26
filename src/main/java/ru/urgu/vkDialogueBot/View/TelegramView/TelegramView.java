package ru.urgu.vkDialogueBot.View.TelegramView;


import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObserver;
import ru.urgu.vkDialogueBot.Events.Signal;
import ru.urgu.vkDialogueBot.View.IView;

import java.util.List;

public class TelegramView extends TelegramLongPollingBot implements IView
{
    @Override
    public void run()
    {
        ApiContextInitializer.init();
        var botsApi = new TelegramBotsApi();
        try
        {
            botsApi.registerBot(this);
        } catch (TelegramApiRequestException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void notify(Signal event)
    {

    }

    @Override
    public void addObserver(IObserver observer)
    {

    }

    @Override
    public void removeObserver(IObserver observer)
    {

    }

    @Override
    public void receive(Signal event)
    {

    }

    @Override
    public void onUpdateReceived(Update update)
    {
        if (update.hasMessage() && update.getMessage().hasText())
        {
            var messageText = update.getMessage().getText();
            var chatId = update.getMessage().getChatId();
            var message = new SendMessage().setChatId(chatId).setText("Got it");
            try
            {
                execute(message);
            } catch (TelegramApiException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates)
    {
        for (var update : updates)
        {
            onUpdateReceived(update);
        }
    }

    @Override
    public String getBotUsername()
    {
        return "Tgtor_bot";
    }

    @Override
    public String getBotToken()
    {
        return "733250136:AAFHRqoIM4kGQZDvs1uiIeW7aytq2rhq64w";
    }
}
