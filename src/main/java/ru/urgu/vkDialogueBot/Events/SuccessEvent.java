package ru.urgu.vkDialogueBot.Events;

import ru.urgu.vkDialogueBot.Controller.IUserToken;

public class SuccessEvent extends Event
{
    public SuccessEvent(IUserToken token)
    {
        super(token);
    }

    @Override
    public String describe()
    {
        return "Каеф";
    }
}
