package ru.urgu.vkDialogueBot.Controller;


import ru.urgu.vkDialogueBot.Events.Event;

public interface IMessenger
{
    void SendMessage(IUserToken userToken, String destination);

    Event CheckMessages(IUserToken userToken);
}
