package ru.urgu.vkDialogueBot.Events;

import lombok.Getter;
import ru.urgu.vkDialogueBot.Controller.IUserToken;

public class FailureEvent extends Event
{
    @Getter
    private String _reason;

    public FailureEvent(IUserToken token, String reason)
    {
        super(token);
        _reason = reason;
    }

    @Override
    public String describe()
    {
        return "Беда :( ::" + " " + _reason;
    }

    @Override
    public Long getTelegramId()
    {
        return null;
    }

    @Override
    public void setTelegramId()
    {

    }
}
