package ru.urgu.vkDialogueBot.Events;

import ru.urgu.vkDialogueBot.Controller.IUserToken;

public class UserLeaveEvent extends Event
{
    public UserLeaveEvent(IUserToken token)
    {
        super(token);
    }

    @Override
    public String describe()
    {
        return null;
    }
}
