package ru.urgu.vkDialogueBot.View.ConsoleView;


import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObserver;
import ru.urgu.vkDialogueBot.Controller.SimpleUserToken;
import ru.urgu.vkDialogueBot.Events.*;
import ru.urgu.vkDialogueBot.View.IView;

import java.util.*;
import java.util.function.Function;

public class ConsoleView implements IView
{
    private final List<IObserver> _observers = new LinkedList<>();
    private final SimpleUserToken _user;
    // Антон - send 147985909
    // Саша - send 161856178
    private ConsoleViewState _currentState = ConsoleViewState.Offline;
    private Map<Class, Function<Event, Event>> _eventProcessMapping = new HashMap<>()
    {
        {
            put(SuccessEvent.class, event -> processSuccess((SuccessEvent) event));
            put(FailureEvent.class, event -> processFailure((FailureEvent) event));
        }

    };

    public ConsoleView()
    {
        _user = new SimpleUserToken(5463728);
        var a = _user.getHash();
    }

    private String readCommand(Scanner scanner)
    {
        System.out.println("Жду команду...");
        return scanner.nextLine();
    }

    private Event parseCommand(String command)
    {
        var fields = command.toLowerCase().split(" ");
        if (command.toLowerCase().equals("help"))
        {
            System.out.println("send *id* *message* - отправить сообщение пользователю с id");
            System.out.println("read *id* - прочитать последние сообщения пользователя с id");
            System.out.println("exit - выход");
        }
        else if (fields[0].toLowerCase().equals("send"))
        {
            var headline = "Сообщение пользователя " + _user.getHash() + ":\n";
            var id = Integer.parseInt(fields[1]);
            var messageBuilder = new StringBuilder();
            for (var i = 2; i < fields.length; i++)
            {
                messageBuilder.append(fields[i]).append(" ");
            }
            return new SendMessageEvent(id, headline + messageBuilder.toString(), _user);
        }
        else if (fields[0].toLowerCase().equals("read"))
        {
            var id = Integer.parseInt(fields[1]);
            return new CheckMessagesEvent(id, _user);
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

    private Event processSuccess(SuccessEvent event)
    {
        if (event.getData() != null)
        {
            var messages = (String[]) event.getData();
            for (var message : messages)
            {
                System.out.println(message);
            }
        }
        System.out.println("Message sent");
        return null;
    }

    private Event processFailure(FailureEvent event)
    {
        System.out.println(event.getUserToken() + event.describe());
        return null;
    }
}