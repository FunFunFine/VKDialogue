package ru.urgu.vkDialogueBot.Model;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import ru.urgu.vkDialogueBot.Controller.IUser;
import ru.urgu.vkDialogueBot.Events.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class VkCommunityModel extends VkModel
{
    private final HashSet<IUser> _users = new HashSet<>();
    private final VkApi _vkApi;

    public VkCommunityModel()
    {
        String accessToken = getTokenFromCfg();
        _vkApi = new VkApi(172735284, accessToken);
    }

    private String getTokenFromCfg()
    {
        var accessToken = "";
        try
        {
            var reader = new BufferedReader(new FileReader("passwords.config"));
            accessToken = reader.readLine().split("=")[1];
        } catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
        return accessToken;
    }

    @Override
    public Event checkMessages(CheckMessagesEvent event)
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
            return new FailureEvent(event.getUserToken(), e.getMessage());
        }
    }

    private int getId(MessageEvent event) throws ClientException, ApiException
    {
        var idType = event.getReceiverType();
        switch (idType)
        {
            case Id:
                return event.getId();
            case ScreenName:
                _vkApi.getId(event.getScreenName());
        }
        return 0;
    }

    @Override
    public Event sendMessage(SendMessageEvent event)
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
            return new FailureEvent(event.getUserToken(), e.getMessage());
        }
    }

    @Override
    public Event addUser(UserCreationEvent event)
    {
        var user = new VkUser(event.getUserToken());
        if (_users.contains(user))
        {
            return new FailureEvent(event.getUserToken(), "User already exists");
        }
        _users.add(user);
        return event;
    }
}
