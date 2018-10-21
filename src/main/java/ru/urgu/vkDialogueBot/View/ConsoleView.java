package ru.urgu.vkDialogueBot.View;


import ru.urgu.vkDialogueBot.Controller.SimpleUserToken;
import ru.urgu.vkDialogueBot.Events.Event;
import ru.urgu.vkDialogueBot.Controller.IObserver;
import ru.urgu.vkDialogueBot.Events.SendMessageEvent;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleView implements IView, IObserver
{
    private GUIState _state = ru.urgu.vkDialogueBot.View.GUIState.STARTED;
    private ConsoleViewState _currentState = ConsoleViewState.Offline;

    private final Map<GUIState, String> _state_menus = new HashMap<GUIState, String>()
    {{
        put(GUIState.UNAUTHORISED, "Введите логин и пароль:");
        put(GUIState.NOTREAD, "1. Проверить сообщения\n2. Выйти");
        put(GUIState.WaitingForCommand, "1. Проверить сообщения\n2. Открыть диалог с юзером\n3. Выйти");
        put(GUIState.INDIALOGUE, "");
        put(GUIState.STARTED, "Привет! Я  - Телеграмматор. Команда \"Help\" расскажет про меня подробнее :) \n Жду команды!");
    }};
    private List<IObserver> _observers = new LinkedList<>();

    private void executeStateMethod(GUIState state)
    {
        if (state == GUIState.UNAUTHORISED)
        {
            authorise();
        }
        else if (state == GUIState.STARTED)
        {
            state = GUIState.WaitingForCommand;
        }
    }

    private void authorise()
    {
        var input = new Scanner(System.in);
        System.out.println("Логин: ");
        var login = input.nextLine();
        System.out.println("Пароль: ");
        var password = input.nextLine();
    }

    public ConsoleView()
    {

    }

    private String readCommand()
    {
        try (var inputScanner = new Scanner(System.in))
        {
            System.out.println("Жду команду...");
            return inputScanner.nextLine();

        }

    }

    /*
    -hello im bot whatcha do?
    -phrase
    -parse phrase
    -answer result
    ask whatcha do

     */
    private Event parseCommand(String command)
    {
        if (command.toLowerCase().equals("help"))
        {
            System.out.println("HA!");
        }
        else if (command.toLowerCase().contains("send"))
        {
            var patternStr = "send ([0-9]+) \"(.+?)\"";
            var pattern = Pattern.compile(patternStr);
            var matcher = pattern.matcher(command.toLowerCase());
            return new SendMessageEvent(Integer.parseInt(matcher.group(1)), matcher.group(2), new SimpleUserToken()); // тут надо на логине создавать SimpleUserToken и его передавать сюда и как-то генерить по логину и паролю или хз, чет такое

        }
        return null;
    }


    @Override
    public void run()
    {
        _currentState = ConsoleViewState.Started;
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
            var command = parseCommand(readCommand());
            if (command != null)
            {
                act(command);
                notify(command);
            }

        }
    }

    private void greetUser()
    {
        System.out.println("Привет! Я  - Телеграмматор. Команда \"Help\" расскажет про меня подробнее :) \n Жду команды!");
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

    }
}
