package ru.urgu.vkDialogueBot.View.ConsoleView;

import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObserver;
import ru.urgu.vkDialogueBot.Events.*;
import ru.urgu.vkDialogueBot.View.IView;

import java.util.*;

public class ConsoleView implements IView
{
    // Антон - send 147985909
    // Саша - send 161856178
    private final List<IObserver> _observers = new LinkedList<>();
    private ConsoleViewState _currentState = ConsoleViewState.Offline;

    private String readCommand(Scanner scanner)
    {
        System.out.println("Жду команду...");
        return scanner.nextLine();
    }

    private void writeText(String text)
    {
        System.out.println(text);
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
                case Working:
                    var msg = readCommand(scanner);
                    notify(new UserIOSignal(msg));
                case Offline:
                    break;
                case Started:
                    notify(new GUIStartedSignal());
                    _currentState = ConsoleViewState.Working;
                    break;
            }
        }
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

    @Override
    public void receive(Signal event)
    {
        if (event instanceof UserIOSignal)
            writeText(((UserIOSignal)event).getText());
        else if (event instanceof GUIExitSignal)
            _currentState = ConsoleViewState.Offline;
    }
}