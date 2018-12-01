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

    @Override
    public Long getTelegramId()
    {
        return null;
    }
}
