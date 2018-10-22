package ru.urgu.vkDialogueBot.Events;

import ru.urgu.vkDialogueBot.Controller.IUserToken;

public class CheckMessagesEvent extends Event
{

    public CheckMessagesEvent(IUserToken token)
    {
        super(token);
    }


    @Override
    public String describe()
    {
        return null;
    }
}
