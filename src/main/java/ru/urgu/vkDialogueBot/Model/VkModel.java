package ru.urgu.vkDialogueBot.Model;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import ru.urgu.vkDialogueBot.Events.*;
import ru.urgu.vkDialogueBot.View.IUser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;

public class VkModel
{
    private final VkApiClient _vk;
    private final GroupActor _actor;
    private final HashSet<IUser> _users = new HashSet<>();
    private final Map<Class, Func<Event, Event>> _eventActionMapping = new HashMap<>()
    {
        {
            put(UserCreationEvent.class, (event) -> addUser((UserCreationEvent) event));
            put(SendMessageEvent.class, (event) -> sendMessage((SendMessageEvent) event));

        }
    };

    public VkModel()
    {
        var transportClient = HttpTransportClient.getInstance();
        _vk = new VkApiClient(transportClient);
        _actor = new GroupActor(172735284, "02fc3f90750ecb59d638f87b1b34eea40d92831ab3ddc00e82205002f82cdfc8bc9c85ca5bd7340672108");

    }

    private Event sendMessage(SendMessageEvent event)
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

    private Event addUser(UserCreationEvent event)
    {
        SimpleUser user = new SimpleUser(event.getUserToken());
        if (_users.contains(user))
        {
            return new FailureEvent(event.getUserToken());
        }
        _users.add(user);
        return new SuccessEvent(event.getUserToken());
    }

    public Event processEvent(Event event)
    {
        return _eventActionMapping.get(event.getClass()).act(event);
    }
}
