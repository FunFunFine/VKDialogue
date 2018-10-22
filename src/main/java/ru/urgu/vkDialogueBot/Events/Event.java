package ru.urgu.vkDialogueBot.Events;

import ru.urgu.vkDialogueBot.Controller.IUserToken;

public abstract class Event
{
    public abstract IUserToken getUserToken();

    public abstract String describe();

}
