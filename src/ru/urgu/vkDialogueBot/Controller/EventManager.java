package ru.urgu.vkDialogueBot.Controller;

import ru.urgu.vkDialogueBot.GUI.*;

import java.util.ArrayList;
import java.util.List;

public class EventManager
{
    private final IMessenger _messenger;
    private List<IUser> _users = new ArrayList<>();

    EventManager(IMessenger messenger) // probably we need to pass model right here to update it
    {
        _messenger = messenger;
    }

    private void addUser(IUser user)
    {
        // add user to model (ask him for login & pass somehow)
        _users.add(user);
    }

    private void removeUser(IUser user)
    {
        //remove user from model
        _users.remove(user);
    }

    public void ResolveEvent(IEvent event)
    {
        //this one looks bad and dirty, we should rework it
        if (event instanceof UserCreationEvent)
        {
            addUser((IUser) event.getData());
        }
        else if (event instanceof UserLeaveEvent)
        {
            removeUser((IUser) event.getData());
        }
        else if (event instanceof SendMessageEvent)
        {
            sendMessage((IMessage) event.getData());
        }
        else if (event instanceof CheckMessagesEvent)
        {
            checkMessages();
        }

    }

    private void checkMessages()
    {

    }

    private void sendMessage(IMessage data)
    {

    }

}
