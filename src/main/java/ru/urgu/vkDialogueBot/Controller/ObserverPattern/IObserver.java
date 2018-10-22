package ru.urgu.vkDialogueBot.Controller.ObserverPattern;

import ru.urgu.vkDialogueBot.Events.Event;

public interface IObserver
{
    void receiveEvent(Event event);
}
