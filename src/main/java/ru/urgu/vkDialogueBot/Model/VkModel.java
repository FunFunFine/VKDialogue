package ru.urgu.vkDialogueBot.Model;

import ru.urgu.vkDialogueBot.Events.CheckMessagesEvent;
import ru.urgu.vkDialogueBot.Events.Event;
import ru.urgu.vkDialogueBot.Events.SendMessageEvent;
import ru.urgu.vkDialogueBot.Events.UserCreationEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class VkModel
{
    private final Map<Class, Function<Event, Event>> _eventActionMapping = new HashMap<>()
    {
        {
            put(UserCreationEvent.class, (event) -> addUser((UserCreationEvent) event));
            put(SendMessageEvent.class, (event) -> sendMessage((SendMessageEvent) event));
            put(CheckMessagesEvent.class, (event) -> checkMessages((CheckMessagesEvent) event));

        }
    };

    protected abstract Event checkMessages(CheckMessagesEvent event);

    protected abstract Event sendMessage(SendMessageEvent event);

    protected abstract Event addUser(UserCreationEvent event);

    public Event processEvent(Event event)
    {
        return _eventActionMapping.get(event.getClass()).apply(event);
    }

}
