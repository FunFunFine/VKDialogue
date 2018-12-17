package ru.urgu.vkDialogueBot.Model;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import org.junit.Before;
import org.junit.Test;
import ru.urgu.vkDialogueBot.Events.CheckMessagesEvent;
import ru.urgu.vkDialogueBot.Events.FailureEvent;
import ru.urgu.vkDialogueBot.Events.SendMessageEvent;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class VkCommunityModel_Should
{
    private static VkApi vkApi;
    private static VkCommunityModel model;

    @Before
    public void SetUp()
    {
        vkApi = mock(VkApi.class);
        model = new VkCommunityModel(vkApi);
    }

    @Test
    public void returnSendMessages_WhenSendingMessages_AndUserIsKnown()
    {
        var id = 1234;
        var message = "message";
        final SendMessageEvent event = new SendMessageEvent(id, message, null);
        var result = model.sendMessage(event);
        assertSame(result, event);
    }

    @Test
    public void returnFailure_WhenSendingMessages_AndUserIsUnknown() throws ClientException, ApiException
    {
        doThrow(ApiException.class).when(vkApi).sendMessage(10001, "me");
        final SendMessageEvent event = new SendMessageEvent(10001, "me", null);
        var result = model.sendMessage(event);
        assertSame(result.getClass(), FailureEvent.class);
    }

    @Test
    public void returnCheckMessages_WhenCheckingMessages_AndUserIsKnown() throws ClientException, ApiException
    {
        var id = 1234;
        var messages = new String[1];
        messages[0] = "message";
        when(vkApi.checkMessage(id, 50, 10)).thenReturn(messages);
        final CheckMessagesEvent event = new CheckMessagesEvent(id, null);
        var result = model.checkMessages(event);
        event.setMessages(messages);
        assertSame(result, event);
    }

    @Test
    public void returnFailure_WhenCheckingMessages_AndUserIsUnknown() throws ClientException, ApiException
    {
        when(vkApi.checkMessage(10001, 50, 10)).thenThrow(ApiException.class);
        final CheckMessagesEvent event = new CheckMessagesEvent(10001, null);
        var result = model.checkMessages(event);
        assertSame(result.getClass(), FailureEvent.class);
    }
}