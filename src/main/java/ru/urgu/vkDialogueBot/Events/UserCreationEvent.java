package ru.urgu.vkDialogueBot.Events;

import ru.urgu.vkDialogueBot.Controller.SimpleUserToken;

public class UserCreationEvent extends Event
{
    public UserCreationEvent(String login, String pass)
    {
        super(new SimpleUserToken(login.hashCode() ^ pass.hashCode()));
    }

    @Override
    public String describe()
    {
        return "Создаем пользователя";
    }

    @Override
    public Long getId()
    {
        return null;
    }
}
