package ru.urgu.vkDialogueBot.Model;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;

public class VkApi implements IVkApi
{

    private final VkApiClient _vk;
    private final GroupActor _actor;

    public VkApi(String token)
    {
        this(172735284, token);
    }

    public VkApi(int groupId, String accessToken)
    {
        var transportClient = HttpTransportClient.getInstance();
        _vk = new VkApiClient(transportClient);
        _actor = new GroupActor(groupId, accessToken);

    }


    @Override
    public void sendMessage(int id, String message) throws ClientException, ApiException
    {
        _vk.messages().send(_actor).userId(id).message(message).execute();
    }

    @Override
    public String[] checkMessage(int id, int count, int oldCount) throws ClientException, ApiException
    {
        var history = _vk.messages().getHistory(_actor).userId(id).count(count).execute();
        var unreadAmount = history.getUnread();
        unreadAmount = unreadAmount == null ? 0 : unreadAmount;
        return history.getItems().stream().limit(unreadAmount + oldCount).map(Message::getBody).toArray(String[]::new);
    }

    @Override
    public int getId(String screenName) throws ClientException, ApiException
    {
        var user = _vk.users().get(_actor).userIds(screenName).execute().get(0);
        return user.getId();

    }
}
