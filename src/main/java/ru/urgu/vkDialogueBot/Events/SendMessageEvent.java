package ru.urgu.vkDialogueBot.Events;

import ru.urgu.vkDialogueBot.Controller.IUserToken;

public class SendMessageEvent extends Event
{
    private int _id;
    private String _message;

    public SendMessageEvent(int id, String message, IUserToken userToken)
    {
        super(userToken);
        _id = id;
        _message = message;
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
