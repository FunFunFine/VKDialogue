package ru.urgu.vkDialogueBot.Events;

import ru.urgu.vkDialogueBot.Controller.IUserToken;

public class FailureEvent extends Event
{
    private String _reason;

    public FailureEvent(IUserToken token, String reason)
    {
        super(token);
        _reason = reason;
    }

    @Override
    public String describe()
    {
        return "Беда :( ::" + " " + _reason;
    }
}
