package ru.urgu.vkDialogueBot.Events;

import ru.urgu.vkDialogueBot.Controller.IUserToken;
import ru.urgu.vkDialogueBot.Controller.SimpleUserToken;

public class UserCreationEvent extends Event
{
    private final SimpleUserToken _userToken;

    public UserCreationEvent(String login, String pass)
    {
        _userToken = new SimpleUserToken(login.hashCode() ^ pass.hashCode());
    }

    @Override
    public IUserToken getUserToken()
    {
        return _userToken;
    }

    @Override
    public String describe()
    {
        return "Создаем пользователя";
    }
}
