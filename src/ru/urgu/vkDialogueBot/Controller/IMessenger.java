package ru.urgu.vkDialogueBot.Controller;

import ru.urgu.vkDialogueBot.GUI.IEvent;

public interface IMessenger
{
    void SendMessage(IUserToken userToken, String destination);

    IEvent CheckMessages(IUserToken userToken);
}
