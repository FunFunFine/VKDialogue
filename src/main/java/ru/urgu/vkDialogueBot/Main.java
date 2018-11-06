package ru.urgu.vkDialogueBot;

import ru.urgu.vkDialogueBot.Controller.BotController;
import ru.urgu.vkDialogueBot.Model.VkApi;
import ru.urgu.vkDialogueBot.Model.VkCommunityModel;
import ru.urgu.vkDialogueBot.View.ConsoleView.ConsoleView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main
{

    public static void main(String[] args)
    {
        var gui = new ConsoleView();
        var vkApi = new VkApi(getTokenFromCfg());
        var controller = new BotController(new VkCommunityModel(vkApi), gui);
        controller.runBot();
    }

    private static String getTokenFromCfg()
    {
        var accessToken = "";
        try
        {
            var reader = new BufferedReader(new FileReader("passwords.config"));
            accessToken = reader.readLine().split("=")[1];
        } catch (IOException e)
        {
            return null;
        }
        return accessToken;
    }
}
