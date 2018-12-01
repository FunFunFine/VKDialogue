package ru.urgu.vkDialogueBot.Events;

import ru.urgu.vkDialogueBot.Controller.IUserToken;

public abstract class Event implements Signal
{
    private IUserToken _userToken;

    public Event(IUserToken token)
    {
        _userToken = token;
    }

    public IUserToken getUserToken()
    {
        return _userToken;
    }

    public abstract String describe();

    @Override
    public Long getId()
    {
        return null;
    }
}
