package ru.urgu.vkDialogueBot.Model;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import ru.urgu.vkDialogueBot.Controller.IUser;
import ru.urgu.vkDialogueBot.Controller.SimpleUser;
import ru.urgu.vkDialogueBot.Events.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;

public class VkCommunityModel extends VkModel
{
    private final VkApiClient _vk;
    private final GroupActor _actor;
    private final HashSet<IUser> _users = new HashSet<>();
    private final Map<Class, Function<Event, Event>> _eventActionMapping = new HashMap<>()
    {
        {
            put(UserCreationEvent.class, (event) -> addUser((UserCreationEvent) event));
            put(SendMessageEvent.class, (event) -> sendMessage((SendMessageEvent) event));
            put(CheckMessagesEvent.class, (event) -> checkMessages((CheckMessagesEvent) event));

        }
    };

    public VkCommunityModel()
    {
        var transportClient = HttpTransportClient.getInstance();
        _vk = new VkApiClient(transportClient);
        _actor = new GroupActor(172735284, "02fc3f90750ecb59d638f87b1b34eea40d92831ab3ddc00e82205002f82cdfc8bc9c85ca5bd7340672108");

    }

    @Override
    protected Event checkMessages(CheckMessagesEvent event)
    {
        return null;
    }

    @Override
    protected Event sendMessage(SendMessageEvent event)
    {

        var message = event.getMessage();
        var id = event.getId();
        try
        {
            _vk.messages().send(_actor).userId(id).message(message).execute();
        } catch (ApiException | ClientException e)
        {
            System.out.println(e);
            return new FailureEvent(event.getUserToken());
        }
        return new SuccessEvent(event.getUserToken());
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
        return new SuccessEvent(event.getUserToken());
    }


}
