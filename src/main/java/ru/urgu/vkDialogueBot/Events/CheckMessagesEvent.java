package ru.urgu.vkDialogueBot.Events;

import ru.urgu.vkDialogueBot.Controller.IUserToken;

public class CheckMessagesEvent extends MessageEvent
{
    public CheckMessagesEvent(int id, IUserToken token)
    {
        this(token, ReceiverType.Id);
        _id = id;
    }

    public CheckMessagesEvent(String name, String surname, IUserToken token)
    {
        this(token, ReceiverType.NameSurname);
        _name = name;
        _surname = surname;
    }

    public CheckMessagesEvent(String screenName, IUserToken token)
    {
        this(token, ReceiverType.ScreenName);
        _screenName = screenName;
    }

    private CheckMessagesEvent(IUserToken token, ReceiverType receiverType)
    {
        super(token, receiverType);
    }

    @Override
    public String describe()
    {
        return null;
    }
}
