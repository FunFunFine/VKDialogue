package ru.urgu.vkDialogueBot.Events;

import ru.urgu.vkDialogueBot.Controller.IUserToken;

public class FailureEvent extends Event
{
    private final IUserToken _userToken;

    public FailureEvent(IUserToken userToken)
    {
        _userToken = userToken;
    }

    @Override
    public IUserToken getUserToken()
    {
        return _userToken;
    }

    @Override
    public String describe()
    {
        return "Беда :(";
    }
}
