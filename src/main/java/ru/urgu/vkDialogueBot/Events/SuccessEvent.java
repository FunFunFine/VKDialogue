package ru.urgu.vkDialogueBot.Events;

import ru.urgu.vkDialogueBot.Controller.BotController;
import ru.urgu.vkDialogueBot.Controller.IUserToken;

public class SuccessEvent extends Event
{
    private final IUserToken _userToken;

    public SuccessEvent(IUserToken userToken)
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
        return "Каеф";
    }
}
