package ru.urgu.vkDialogueBot.Events;

import ru.urgu.vkDialogueBot.Controller.IUserToken;

public class GetHelpEvent extends Event
{
    public GetHelpEvent(IUserToken token)
    {
        super(token);
    }

    @Override
    public String describe()
    {
        return null;
    }
}
