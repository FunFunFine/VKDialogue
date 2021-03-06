package ru.urgu.vkDialogueBot.Events;

import lombok.Getter;
import lombok.Setter;
import ru.urgu.vkDialogueBot.Controller.IUserToken;

public class CheckMessagesEvent extends MessageEvent
{
    @Setter
    @Getter
    private String[] _messages = null;

    @Setter
    @Getter
    private int _oldMessagesAmount = 0;

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

    @Override
    public void setTelegramId(Long chatId)
    {

    }
}
