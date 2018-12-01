package ru.urgu.vkDialogueBot.Events;

import lombok.Getter;
import ru.urgu.vkDialogueBot.Controller.IUserToken;

public class GetHelpEvent extends Event
{
    @Getter
    private String _message;

    public GetHelpEvent(IUserToken token, String message)
    {
        super(token);
        this._message = message;
    }


    @Override
    public String describe()
    {
        return this.getClass().getName();
    }
}
