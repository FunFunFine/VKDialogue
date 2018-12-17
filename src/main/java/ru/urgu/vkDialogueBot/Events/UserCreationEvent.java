package ru.urgu.vkDialogueBot.Events;

import ru.urgu.vkDialogueBot.Controller.SimpleUserToken;

public class UserCreationEvent extends Event
{
    private Long chatId;

    public UserCreationEvent(String login, String pass)
    {
        super(new SimpleUserToken((long) login.hashCode() ^ pass.hashCode()));
    }

    @Override
    public String describe()
    {
        return "Создаем пользователя";
    }

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
