package ru.urgu.vkDialogueBot.Controller;

import lombok.Getter;
import lombok.Setter;

public class SimpleUserToken implements IUserToken
{
    @Getter
    private final int _hash;

    @Getter
    @Setter
    private int _currentResponderId = -1;

    public SimpleUserToken(int hash)
    {
        _hash = hash;
    }

//    public int getHash()
//    {
//        return _hash;
//    }
}
