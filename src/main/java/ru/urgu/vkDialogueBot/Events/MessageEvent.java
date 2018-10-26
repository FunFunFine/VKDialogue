package ru.urgu.vkDialogueBot.Events;

import lombok.experimental.NonFinal;
import ru.urgu.vkDialogueBot.Controller.IUserToken;

public abstract class MessageEvent extends Event
{
    public final ReceiverType _receiverType;
    @NonFinal
    String _screenName = null;
    @NonFinal
    String _name = null;
    @NonFinal
    String _surname = null;
    @NonFinal
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
