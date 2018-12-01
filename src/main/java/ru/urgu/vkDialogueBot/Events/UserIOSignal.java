package ru.urgu.vkDialogueBot.Events;

import lombok.Getter;

public class UserIOSignal implements Signal
{
    @Getter
    private String _text;

    public UserIOSignal(String text)
    {
        _text = text;
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
