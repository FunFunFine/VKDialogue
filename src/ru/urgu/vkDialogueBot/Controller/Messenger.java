package ru.urgu.vkDialogueBot.Controller;

import ru.urgu.vkDialogueBot.GUI.IEvent;

public class Messenger implements IMessenger
{

    @Override
    public void SendMessage(IUserToken userToken, String destination)
    {

    }

    @Override
    public IEvent CheckMessages(IUserToken userToken)
    {
        return null;
    }
}
