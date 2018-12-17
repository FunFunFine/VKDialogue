package ru.urgu.vkDialogueBot.Model;

import ru.urgu.vkDialogueBot.Events.CheckMessagesEvent;
import ru.urgu.vkDialogueBot.Events.Event;
import ru.urgu.vkDialogueBot.Events.SendMessageEvent;
import ru.urgu.vkDialogueBot.Events.UserCreationEvent;

public interface IVkModel
{
    Event checkMessages(CheckMessagesEvent event);

    Event sendMessage(SendMessageEvent event);

    Event addUser(UserCreationEvent event);

}
