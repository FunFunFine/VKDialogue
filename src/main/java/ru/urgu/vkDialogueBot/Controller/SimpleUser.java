package ru.urgu.vkDialogueBot.Controller;

public class SimpleUser implements IUser
{
    private final IUserToken _token;

    public SimpleUser(IUserToken userToken)
    {
        _token = userToken;

    }
}
