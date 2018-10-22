package ru.urgu.vkDialogueBot.View.ConsoleView;


import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObserver;
import ru.urgu.vkDialogueBot.Controller.SimpleUserToken;
import ru.urgu.vkDialogueBot.Events.Event;
import ru.urgu.vkDialogueBot.Events.FailureEvent;
import ru.urgu.vkDialogueBot.Events.SendMessageEvent;
import ru.urgu.vkDialogueBot.View.IView;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class ConsoleView implements IView
{
    private final List<IObserver> _observers = new LinkedList<>();
    private final SimpleUserToken _user;
    //Антон - send 147985909 VCD
    // Я - send 161856178 macho
    private ConsoleViewState _currentState = ConsoleViewState.Offline;

    public ConsoleView()
    {
        _user = new SimpleUserToken(5463728);
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
            System.out.println("exit - выход");
        }
        else if (fields[0].toLowerCase().equals("send"))
        {
//            if (_user == null)
//            {
//                throw new NullPointerException("_user is null");
//            }

//            var patternStr = "send (?<id>[0-9]+) (?<msg>.+)";
//            var pattern = Pattern.compile(patternStr);
//            var m = pattern.matcher(command.toLowerCase());
//            var strId = m.group("id");
//            var id = Integer.parseInt(strId);
//            var message = m.group("msg");
            var headline = "Сообщение пользователя " + _user.getHash() + ":\n";
            var id = Integer.parseInt(fields[1]);
            var messageBuilder = new StringBuilder();
            for (var i = 2; i < fields.length; i++)
            {
                messageBuilder.append(fields[i]).append(" ");
            }

            return new SendMessageEvent(id, headline + messageBuilder.toString(), _user);
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
        if (event.getClass().isInstance(FailureEvent.class))
        {
            var failureEvent = (FailureEvent) event;
            System.out.println(failureEvent.getUserToken() + failureEvent.describe());
        }
        else
        {
            System.out.println("Message sent");
        }
    }
}