package ru.urgu.vkDialogueBot.Events;

import lombok.Getter;

public class UserIOSignal implements Signal
{
    @Getter
    private String text;

    public UserIOSignal(String text)
    {
        this.text = text;
    }
}
