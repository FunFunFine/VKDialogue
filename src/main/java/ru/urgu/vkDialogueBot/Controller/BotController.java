package ru.urgu.vkDialogueBot.Controller;

import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObservable;
import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObserver;
import ru.urgu.vkDialogueBot.Events.Signal;
import ru.urgu.vkDialogueBot.Events.UserIOSignal;
import ru.urgu.vkDialogueBot.Model.VkCommunityModel;
import ru.urgu.vkDialogueBot.View.Command;
import ru.urgu.vkDialogueBot.View.IView;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class BotController implements IObserver, IObservable
{
    private final IView _gui;
    private final VkCommunityModel _model;
    private LinkedList<IObserver> _observers = new LinkedList<>();
    private final CommandParser _parser;
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
        _model = vkModel;
        _gui = gui;
    }


    public void runBot()
    {
        _gui.run();
    }

    private void greetUser()
    {
        System.out.println("Привет! Я  - Телеграмматор. Команда \"Help\" расскажет про меня подробнее :)");
    }

    @Override
    public void receive(Signal event)
    {
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
