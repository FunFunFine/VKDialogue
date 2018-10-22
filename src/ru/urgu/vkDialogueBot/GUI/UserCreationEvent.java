package ru.urgu.vkDialogueBot.GUI;

public class UserCreationEvent implements IEvent
{
    public final String Login;
    public final String Password;

    public UserCreationEvent(String login, String pass)
    {
        Login = login;
        Password = pass;
    }

    @Override
    public Object getData()
    {
        return null;
    }
}
