package ru.urgu.vkDialogueBot.Events;

import ru.urgu.vkDialogueBot.Controller.IUserToken;

public class FailureEvent extends Event
{
    public FailureEvent(IUserToken token)
    {
        super(token);
    }

    @Override
    public String describe()
    {
        return "Беда :(";
    }
}
