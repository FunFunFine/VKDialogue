package ru.urgu.vkDialogueBot.View.ConsoleView;


import org.jetbrains.annotations.NotNull;
import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObserver;
import ru.urgu.vkDialogueBot.Controller.SimpleUserToken;
import ru.urgu.vkDialogueBot.Events.CheckMessagesEvent;
import ru.urgu.vkDialogueBot.Events.Event;
import ru.urgu.vkDialogueBot.Events.FailureEvent;
import ru.urgu.vkDialogueBot.Events.SendMessageEvent;
import ru.urgu.vkDialogueBot.Utils.Action;
import ru.urgu.vkDialogueBot.View.IView;

import java.util.*;

public class ConsoleView implements IView
{
    private final List<IObserver> _observers = new LinkedList<>();
    private final SimpleUserToken _user;
    // Антон - send 147985909
    // Саша - send 161856178
    private ConsoleViewState _currentState = ConsoleViewState.Offline;
    private Map<Class, Action<Event>> _eventProcessMapping = new HashMap<>()
    {
        {
            put(SendMessageEvent.class, event -> processSendMessages((SendMessageEvent) event));
            put(CheckMessagesEvent.class, event -> processCheckMessages((CheckMessagesEvent) event));
            put(FailureEvent.class, event -> processFailure((FailureEvent) event));
        }
    };

    public ConsoleView()
    {
        _user = new SimpleUserToken(5463728);
    }

    private void processCheckMessages(CheckMessagesEvent event)
    {
        var messages = event.getMessages();
        for (var message : messages)
        {
            System.out.println(message);
        }
    }

    private void processSendMessages(SendMessageEvent event)
    {
        System.out.println("Message sent");
    }

    private String readCommand(Scanner scanner)
    {
        System.out.println("Жду команду...");
        return scanner.nextLine();
    }

    private Event parseCommand(String command)
    {
        var fields = command.toLowerCase().trim().split(" ");
        var doesIdExists = _user.getCurrentResponderId() != -1;
        if (fields[0].equals("help"))
        {
            ShowHelp();
        }
        else if (fields[0].toLowerCase().equals("send"))
        {
            if (!doesIdExists)
            {
                System.out.println("Нужно сделать set *id*");
                return null;
            }
            return SendMessage(fields);
        }
        else if (fields[0].toLowerCase().equals("read"))
        {
            if (!doesIdExists)
            {
                System.out.println("Нужно сделать set *id*");
                return null;
            }
            var event = new CheckMessagesEvent(_user.getCurrentResponderId(), _user);
            event.setOldMessagesAmount(10);
            return event;
        }
        else if (fields[0].toLowerCase().equals("set") || fields[0].toLowerCase().equals("change"))
        {
            _user.setCurrentResponderId(Integer.parseInt(fields[1]));
        }
        else if (command.toLowerCase().equals("exit"))
        {
            System.out.println("До свидания");
            _currentState = ConsoleViewState.Offline;
        }
        else
        {
            System.out.println("Я не знаю такую команду");
        }
        return null;
    }


    @NotNull
    private Event SendMessage(String[] fields)
    {
        var headline = "Сообщение пользователя " + _user.getHash() + ":\n";
        var messageBuilder = new StringBuilder();
        for (var i = 1; i < fields.length; i++)
        {
            messageBuilder.append(fields[i]).append(" ");
        }
        return new SendMessageEvent(_user.getCurrentResponderId(), headline + messageBuilder.toString(), _user);
    }

    private void ShowHelp()
    {
        System.out.println("send *message* - отправить сообщение пользователю в текущий диалог");
        System.out.println("create *id* - создать диалог с пользователем *id*");
        System.out.println("set *id* - переключиться на диалог с пользователем *id*");
        System.out.println("read *n* - прочитать все новые + n старых сообщений из текущего диалога (default(n) = 10)");
        System.out.println("exit - выход");
    }


    @Override
    public void run()
    {
        _currentState = ConsoleViewState.Started;
        var scanner = new Scanner(System.in);
        while (!_currentState.equals(ConsoleViewState.Offline))
        {
            switch (_currentState)
            {
                case Waiting:
                    break;
                case Working:
                    break;
                case Offline:
                    break;
                case Started:
                    greetUser();
                    _currentState = ConsoleViewState.Waiting;
                    break;
            }
            var command = parseCommand(readCommand(scanner));
            if (command != null)
            {
                act(command);
                notify(command);
            }
        }
    }

    private void greetUser()
    {
        System.out.println("Привет! Я  - Телеграмматор. Команда \"Help\" расскажет про меня подробнее :)");
    }

    private void act(Event command)
    {
    }


    @Override
    public void notify(Event event)
    {
        for (var observer : _observers)
        {
            observer.receiveEvent(event);
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
    public void receiveEvent(Event event)
    {
        _eventProcessMapping.get(event.getClass()).apply(event);

    }

    private void processFailure(FailureEvent event)
    {
        System.out.println(event.getUserToken() + event.describe());
    }
}