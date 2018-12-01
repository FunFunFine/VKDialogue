package ru.urgu.vkDialogueBot.Events;

import lombok.Getter;
import ru.urgu.vkDialogueBot.Controller.IUserToken;

public class SetUserEvent extends Event
{
    @Getter
    private int _id;

    public SetUserEvent(IUserToken token, int id)
    {
        super(token);
        _id = id;
    }

    @Override
    public String describe()
    {
        return null;
    }

    private Long chatId;

    @Override
    public Long getTelegramId()
    {
        return chatId;
    }

    @Override
    public void setTelegramId(Long chatId)
    {
        this.chatId = chatId;
    }
}
