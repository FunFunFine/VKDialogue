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
        try
        {
            var id = getId(event);
            var history = _vk.messages().getHistory(_actor).userId(id).offset(-7).startMessageId(-1).count(50).execute();
            var unreadAmount = history.getUnread();
            var messages = history.getItems().stream().limit(unreadAmount).map(Message::getBody).toArray(String[]::new);
            var result = new SuccessEvent(event.getUserToken());
            result.setData(messages);
            return result;
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
                var user = _vk.users().get(_actor).userIds(event.getScreenName()).execute().iterator().next();
                return user.getId();
        }
        return 0;
    }

    private int findUser(String name, String surname)
    {
        return 0;
    }

    @Override
    protected Event sendMessage(SendMessageEvent event)
    {
        var message = event.getMessage();
        try
        {
            var id = getId(event);
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
