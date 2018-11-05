package ru.urgu.vkDialogueBot.Controller;

import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObservable;
import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObserver;
import ru.urgu.vkDialogueBot.Events.Event;
import ru.urgu.vkDialogueBot.Events.Signal;
import ru.urgu.vkDialogueBot.Model.VkCommunityModel;
import ru.urgu.vkDialogueBot.View.IView;

import java.util.LinkedList;

public class BotController implements IObserver, IObservable
{
    private final IView _gui;
    private final VkCommunityModel _model;
    private LinkedList<IObserver> _observers = new LinkedList<>();


    public BotController(VkCommunityModel vkModel, IView gui)
    {
        _model = vkModel;
        _gui = gui;
    }


    public void runBot()
    {
        _gui.run();
    }


    @Override
    public void receive(Signal event)
    {
        var resultEvent = _model.processEvent(event);
        notify(resultEvent);
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
