package ru.urgu.vkDialogueBot.Controller.ObserverPattern;

import ru.urgu.vkDialogueBot.Events.Event;
import ru.urgu.vkDialogueBot.Events.Signal;

public interface IObservable
{
    void notify(Signal event);

    void addObserver(IObserver observer);

    void removeObserver(IObserver observer);
}
