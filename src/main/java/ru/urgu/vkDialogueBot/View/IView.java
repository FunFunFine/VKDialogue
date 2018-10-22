package ru.urgu.vkDialogueBot.View;


import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObservable;
import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObserver;

public interface IView extends IObservable, IObserver
{
    void run();
}
