package ru.urgu.vkDialogueBot.Controller.ObserverPattern;

import ru.urgu.vkDialogueBot.Events.Event;

public interface IObservable
{
    void notify(Event event);

    void addObserver(IObserver observer);

    void removeObserver(IObserver observer);
}
