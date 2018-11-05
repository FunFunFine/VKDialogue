package ru.urgu.vkDialogueBot.Model;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;

public interface IVkApi
{

    void sendMessage(int id, String message) throws ClientException, ApiException;

    String[] checkMessage(int id, int count, int oldCount) throws ClientException, ApiException;

    int getId(String screenName) throws ClientException, ApiException;

}
