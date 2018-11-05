package ru.urgu.vkDialogueBot.Controller;

import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObservable;
import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObserver;
import ru.urgu.vkDialogueBot.Events.*;
import ru.urgu.vkDialogueBot.Model.VkCommunityModel;
import ru.urgu.vkDialogueBot.View.Command;
import ru.urgu.vkDialogueBot.View.IView;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import lombok.Getter;

public class BotController implements IObserver, IObservable
{
    private final IView _gui;
    private final VkCommunityModel _model;
    private LinkedList<IObserver> _observers = new LinkedList<>();
    private final CommandParser _parser;
    private final SimpleUserToken _user;

    private Set<Command> _commands = new HashSet<>()
    {
        {
            add(new Command("help", fields -> ShowHelp(fields)));
            add(new Command("set", fields -> SetUser(fields)));
            add(new Command("send", fields -> SendMessage(fields)));
            add(new Command("read", fields -> ReadMessages(fields)));
            add(new Command("exit", fields -> Exit(fields)));
        }
    };

    private Signal Exit(String[] args)
    {
        //System.out.println("До свидания");
        return new GUIExitSignal();
    }

    private Signal ReadMessages(String[] args)
    {
        if (args.length != 0)
            return new UserIOSignal("Неизвестная команда");
        if (_user.getCurrentResponderId() == -1)
            return new UserIOSignal("Нужно сделать set *id*");
        var event = new CheckMessagesEvent(_user.getCurrentResponderId(), _user);
        event.setOldMessagesAmount(10);
        return event;
    }

    private Signal SendMessage(String[] fields)
    {
        if (fields.length == 0)
            return new UserIOSignal("Зачем посылать пустое сообщение?");
        if (_user.getCurrentResponderId() == -1)
            return new UserIOSignal("Нужно сделать set *id*");
        var headline = "Сообщение пользователя " + _user.getHash() + ":\n";
        var messageBuilder = new StringBuilder();
        for (String field : fields) {
            messageBuilder.append(field).append(" ");
        }
        return new SendMessageEvent(_user.getCurrentResponderId(), headline + messageBuilder.toString(), _user);
    }

    private Signal ShowHelp(String[] args)
    {
        var message = "";
        if (args.length != 0)
        {
            message = "Неизвестная команда";
        }
        else
        {
            message = "send *message* - отправить сообщение пользователю в текущий диалог\n" +
                    "set *id* - переключиться на диалог с пользователем *id*\n" +
                    "read - прочитать все новые + 10 старых сообщений из текущего диалога\n" +
                    "funfunfine.github.io - здесь можно разрешить нам писать сообщения вам ВК\n" +
                    "exit - выход\n";
        }
        return new UserIOSignal(message);
    }

    private Signal SetUser(String[] args)
    {
        var id = 0;
        try
        {
            id = Integer.parseInt(args[0]);
        } catch (Exception e)
        {
            return new UserIOSignal("Неверный id");
        }
        _user.setCurrentResponderId(id);
        return null;
    }

    public BotController(VkCommunityModel vkModel, IView gui)
    {
        _parser = new CommandParser();
        for (var command:_commands)
            _parser.addCommand(command);
        _model = vkModel;
        _gui = gui;
        _user = new SimpleUserToken(5463728);
    }


    public void runBot()
    {
        _gui.run();
    }

    private Signal greetUser()
    {
        return new UserIOSignal("Привет! Я  - Телеграмматор. Команда \"Help\" расскажет про меня подробнее :)");
    }

    private void processSend(Event signal)
    {
        var response = _model.processEvent(signal);
        UserIOSignal responseSignal;
        if (response instanceof FailureEvent)
            responseSignal = new UserIOSignal(response.describe());
        else
            responseSignal = new UserIOSignal("Отправлено");
        notify(responseSignal);
    }

    private void processCheck(Event signal)
    {
        var response = _model.processEvent(signal);
        UserIOSignal responseSignal;
        if (response instanceof FailureEvent)
            responseSignal = new UserIOSignal(response.describe());
        else {
            var builder = new StringBuilder();
            for(var s:((CheckMessagesEvent) response).getMessages())
            {
                builder.append(s);
            }
            responseSignal = new UserIOSignal(builder.toString());
        }
        notify(responseSignal);
    }

    private void parseText(String text)
    {
        var signal = _parser.parse(text);
        if (signal == null)
            return;
        if (signal instanceof UserIOSignal)
            notify(signal);
        else if (signal instanceof SendMessageEvent)
        {
            processSend((SendMessageEvent)signal);
        }
        else if(signal instanceof GUIExitSignal)
        {
            notify(signal);
        }
        else if(signal instanceof CheckMessagesEvent)
        {
            processCheck((CheckMessagesEvent)signal);
        }
    }

    @Override
    public void receive(Signal event)
    {
        if (event instanceof GUIStartedSignal)
            notify(greetUser());
        else if (event instanceof UserIOSignal)
            parseText(((UserIOSignal)event).getText());
        //var resultEvent = _model.processEvent(event);
        //notify(resultEvent);
    }

    @Override
    public void notify(Signal event)
    {
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

}
