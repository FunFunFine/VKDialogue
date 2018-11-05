package ru.urgu.vkDialogueBot.Model;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import ru.urgu.vkDialogueBot.Controller.IUser;
import ru.urgu.vkDialogueBot.Controller.SimpleUser;
import ru.urgu.vkDialogueBot.Events.*;

import java.util.HashSet;

public class VkCommunityModel extends VkModel
{

    private final HashSet<IUser> _users = new HashSet<>();
    private final VkApi _vkApi;

    public VkCommunityModel()
    {
        _vkApi = new VkApi();
    }

    @Override
    protected Event checkMessages(CheckMessagesEvent event)
    {
        try
        {
            var id = getId(event);
            var messages = _vkApi.checkMessage(id, 50, 10);
            event.setMessages(messages);
            return event;
        } catch (ApiException | ClientException e)
        {
            System.out.println(e);
            return new FailureEvent(event.getUserToken());
        }
    }

    private int getId(MessageEvent event) throws ClientException, ApiException
    {
        var idType = event.getReceiverType();
        switch (idType)
        {
            case Id:
                return event.getId();
            case NameSurname:
                return findUser(event.getName(), event.getSurname());
            case ScreenName:
                _vkApi.getId(event.getScreenName());
        }
        return 0;
    }

    @Override
    protected Event sendMessage(SendMessageEvent event)
    {
        var message = event.getMessage();
        try
        {
            var id = getId(event);
            _vkApi.sendMessage(id, message);
            return event;

        } catch (ApiException | ClientException e)
        {
            System.out.println(e);
            return new FailureEvent(event.getUserToken());
        }
    }

    @Override
    protected Event addUser(UserCreationEvent event)
    {
        SimpleUser user = new SimpleUser(event.getUserToken());
        if (_users.contains(user))
        {
            return new FailureEvent(event.getUserToken());
        }
        _users.add(user);
        return event;
    }

    private int findUser(String name, String surname)
    {
        return 0;
    }


}
