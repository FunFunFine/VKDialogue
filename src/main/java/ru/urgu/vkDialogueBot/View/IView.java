package ru.urgu.vkDialogueBot.View;


import ru.urgu.vkDialogueBot.Controller.IObservable;
import ru.urgu.vkDialogueBot.Controller.IObserver;

public interface IView extends IObservable, IObserver
{
    void run();
}
