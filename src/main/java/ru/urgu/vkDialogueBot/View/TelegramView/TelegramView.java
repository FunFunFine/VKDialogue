package ru.urgu.vkDialogueBot.View.TelegramView;


import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObserver;
import ru.urgu.vkDialogueBot.Events.FailureEvent;
import ru.urgu.vkDialogueBot.Events.Signal;
import ru.urgu.vkDialogueBot.Events.UserIOSignal;
import ru.urgu.vkDialogueBot.View.IView;
import ru.urgu.vkDialogueBot.View.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TelegramView extends TelegramLongPollingBot implements IView
{
    private final List<IObserver> _observers = new LinkedList<>();
    private final HashMap<Long, String> _lastMessages = new HashMap<>();
    private Logger logger;

    public TelegramView(Logger logger)
    {

        this.logger = logger;
    }

    private static ReplyKeyboardMarkup getMainMenuKeyboard()
    {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("отправить");
        keyboardFirstRow.add("выбрать_получателя");
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add("помощь");
        keyboardSecondRow.add("прочитать");
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    @Override
    public void run()
    {
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
        logger.Log("notifying event:"+event.getClass().getName());
        for (var observer : _observers)
        {
            observer.receive(event);
        }
    }

    @Override
    public void addObserver(IObserver observer)
    {
        _observers.add(observer);
    }

    @Override
    public void removeObserver(IObserver observer)
    {
        _observers.remove(observer);
    }

    @Override
    public void receive(Signal event)
    {
        logger.Log("receiving event:"+event.getClass().getName());

        if (event instanceof UserIOSignal)
        {
            var ioSignal = (UserIOSignal) event;
            sendMessage(ioSignal.getText(), event.getTelegramId());
        }
        if (event instanceof FailureEvent)
        {
            var ioSignal = (FailureEvent) event;
            sendMessage(ioSignal.getReason(), event.getTelegramId());
        }
    }

    private void sendMessage(String text, Long chatId)
    {
        var message = new SendMessage().setChatId(chatId)
                                       .setText(text)
                                       .setReplyMarkup(getMainMenuKeyboard());
        try
        {
            execute(message);
        } catch (TelegramApiException e)
        {
            logger.Error(e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        var chatId = update.getMessage().getChatId();
        if (!update.hasMessage() || !update.getMessage().hasText())
        {
            sendMessage(":)", chatId);
            return;
        }
        var messageText = update.getMessage().getText();
        logger.Log("got a message: "+ messageText);

        Signal signal;

        synchronized (_lastMessages)
        {
            switch (messageText)
            {
                case ("/start"):
                    return;
                case ("отправить"):
                case ("выбрать_получателя"):
                    _lastMessages.put(chatId, messageText);
                    sendMessage("Аргументы пажаласта", chatId);
                    return;
                default:
                    var lastMessage = _lastMessages.get(chatId);
                    if (lastMessage == null || lastMessage.equals("выбрать_получателя") || lastMessage.equals("отправить"))
                    {
                        break;
                    }
                    signal = new UserIOSignal(messageText);
                    signal.setTelegramId(chatId);
                    notify(signal);
                    break;
            }
            var lastMessage = _lastMessages.get(chatId);
            if (lastMessage == null)
            {
                _lastMessages.put(chatId, messageText);
                return;
            }
            switch (lastMessage)
            {
                case ("отправить"):
                    signal = new UserIOSignal("отправить " + messageText);
                    signal.setTelegramId(chatId);
                    notify(signal);
                    break;
                case ("выбрать_получателя"):
                    signal = new UserIOSignal("выбрать_получателя " + messageText);
                    signal.setTelegramId(chatId);
                    notify(signal);
                    break;
            }
            _lastMessages.put(chatId, messageText);
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

        return System.getenv("TG_TOKEN");
    }
}
