package ru.urgu.vkDialogueBot.Model;

import ru.urgu.vkDialogueBot.Controller.IUserToken;
import ru.urgu.vkDialogueBot.View.IUser;

public class SimpleUser implements IUser
{
    private final IUserToken _token;

    public SimpleUser(IUserToken userToken)
    {
        _token = userToken;

    }
}
