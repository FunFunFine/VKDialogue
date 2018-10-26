package ru.urgu.vkDialogueBot.Controller;

import lombok.Getter;

public class SimpleUserToken implements IUserToken
{
    @Getter
    private final int _hash;

    public SimpleUserToken(int hash)
    {
        _hash = hash;
    }

//    public int getHash()
//    {
//        return _hash;
//    }
}
