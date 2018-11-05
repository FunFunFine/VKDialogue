package ru.urgu.vkDialogueBot.Events;

import ru.urgu.vkDialogueBot.Controller.IUserToken;

public abstract class MessageEvent extends Event
{
    private final ReceiverType _receiverType;
    String _screenName = null;
    String _name = null;
    String _surname = null;
    int _id = 0;

    MessageEvent(IUserToken token, ReceiverType receiverType)
    {
        super(token);
        _receiverType = receiverType;
    }

    public MessageEvent.ReceiverType getReceiverType()
    {
        return this._receiverType;
    }

    public String getScreenName()
    {
        return this._screenName;
    }

    public String getName()
    {
        return this._name;
    }

    public String getSurname()
    {
        return this._surname;
    }

    public int getId()
    {
        return this._id;
    }

    public enum ReceiverType
    {
        Id,
        NameSurname,
        ScreenName
    }
}
