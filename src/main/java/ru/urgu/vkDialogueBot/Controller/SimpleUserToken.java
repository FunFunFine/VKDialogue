package ru.urgu.vkDialogueBot.Controller;

public class SimpleUserToken implements IUserToken
{
    private final int _hash;

    public SimpleUserToken(int hash)
    {
        _hash = hash;
    }

}
