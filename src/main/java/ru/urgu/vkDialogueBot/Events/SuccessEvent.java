package ru.urgu.vkDialogueBot.Events;

import lombok.Getter;
import lombok.Setter;
import ru.urgu.vkDialogueBot.Controller.IUserToken;

public class SuccessEvent extends Event
{
    public SuccessEvent(IUserToken token)
    {
        super(token);
    }

    @Getter
    @Setter
    private Object _data = null;
    @Override
    public String describe()
    {
        return "Каеф";
    }
}
