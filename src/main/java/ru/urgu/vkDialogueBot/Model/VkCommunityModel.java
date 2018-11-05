package ru.urgu.vkDialogueBot.Model;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import ru.urgu.vkDialogueBot.Controller.IUser;
import ru.urgu.vkDialogueBot.Controller.SimpleUser;
import ru.urgu.vkDialogueBot.Events.*;

import java.io.*;
import java.util.HashSet;

public class VkCommunityModel extends VkModel
{
    private final VkApiClient _vk;
    private final GroupActor _actor;
    private final HashSet<IUser> _users = new HashSet<>();
    private final VkApi _vkApi;

    public VkCommunityModel()
    {
        var transportClient = HttpTransportClient.getInstance();
        _vk = new VkApiClient(transportClient);
        var accessToken = "";
        try
        {
            var reader = new BufferedReader(new FileReader("passwords.config"));
            accessToken = reader.readLine().split("=")[1];
        }catch (IOException e)
        {
            System.out.println(e);
        }
        _actor = new GroupActor(172735284, accessToken);

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
            return new FailureEvent(event.getUserToken(), e.getMessage());
        }
    }

    @Override
    protected Event addUser(UserCreationEvent event)
    {
        SimpleUser user = new SimpleUser(event.getUserToken());
        if (_users.contains(user))
        {
            return new FailureEvent(event.getUserToken(), "User already exists");
        }
        _users.add(user);
        return event;
    }

    private int findUser(String name, String surname)
    {
        return 0;
    }


}
