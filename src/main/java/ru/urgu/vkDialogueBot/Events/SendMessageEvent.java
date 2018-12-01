package ru.urgu.vkDialogueBot.Events;

import lombok.EqualsAndHashCode;
import lombok.Value;
import ru.urgu.vkDialogueBot.Controller.IUserToken;


@EqualsAndHashCode(callSuper = true)
@Value
public class SendMessageEvent extends MessageEvent
{


    private String _message;

    public SendMessageEvent(int id, String message, IUserToken token)
    {
        this(message, token, ReceiverType.Id);
        _id = id;
    }

    public SendMessageEvent(String name, String surname, String message, IUserToken token)
    {
        this(message, token, ReceiverType.NameSurname);
        _name = name;
        _surname = surname;
    }


    public SendMessageEvent(String screenName, String message, IUserToken token)
    {
        this(message, token, ReceiverType.ScreenName);
        _screenName = screenName;
    }

    private SendMessageEvent(String message, IUserToken token, ReceiverType receiverType)
    {
        super(token, receiverType);
        _message = message;
    }


    @Override
    public String describe()
    {
        return String.format("Отправляем %s для id %s", _message, _id);
    }


    @Override
    public void setTelegramId(Long chatId)
    {

    }
}
