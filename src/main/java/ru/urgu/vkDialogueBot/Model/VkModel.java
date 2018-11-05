package ru.urgu.vkDialogueBot.Model;

import ru.urgu.vkDialogueBot.Events.CheckMessagesEvent;
import ru.urgu.vkDialogueBot.Events.Event;
import ru.urgu.vkDialogueBot.Events.SendMessageEvent;
import ru.urgu.vkDialogueBot.Events.UserCreationEvent;

public abstract class VkModel
{


    public abstract Event checkMessages(CheckMessagesEvent event);

    public abstract Event sendMessage(SendMessageEvent event);

    public abstract Event addUser(UserCreationEvent event);


}
