package ru.urgu.vkDialogueBot.Controller.ObserverPattern;

import ru.urgu.vkDialogueBot.Events.Signal;

public interface IObserver
{
    void receive(Signal event);
}
