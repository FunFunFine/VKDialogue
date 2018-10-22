package ru.urgu.vkDialogueBot.Controller;


import ru.urgu.vkDialogueBot.Events.Event;

public class Messenger implements IMessenger
{

    @Override
    public void SendMessage(IUserToken userToken, String destination)
    {

    }

    @Override
    public Event CheckMessages(IUserToken userToken)
    {
        return null;
    }
}
