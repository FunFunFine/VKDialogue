package ru.urgu.vkDialogueBot.Events;

import lombok.Data;
import ru.urgu.vkDialogueBot.Controller.IUserToken;


public class SendMessageEvent extends Event
{
    private int _id;
    private String _message;
    private IUserToken _userToken;

    public SendMessageEvent(int id, String message, IUserToken userToken)
    {
        _id = id;
        _message = message;
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
        return String.format("Отправляем %s для id %s", _message, _id);
    }

    public String getMessage()
    {
        return _message;
    }

    public int getId()
    {
        return _id;
    }
}
