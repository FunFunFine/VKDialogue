package ru.urgu.vkDialogueBot;

import org.telegram.telegrambots.ApiContextInitializer;
import ru.urgu.vkDialogueBot.Controller.BotController;
import ru.urgu.vkDialogueBot.Model.VkApi;
import ru.urgu.vkDialogueBot.Model.VkCommunityModel;
import ru.urgu.vkDialogueBot.View.TelegramView.TelegramView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main
{

    public static void main(String[] args)
    {
        ApiContextInitializer.init();

        TelegramView gui = new TelegramView();
        VkApi vkApi = new VkApi(getTokenFromCfg());
        BotController controller = new BotController(new VkCommunityModel(vkApi), gui);
        controller.runBot();
    }

    private static String getTokenFromCfg()
    {
        String accessToken = "";
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader("passwords.config"));
            accessToken = reader.readLine().split("=")[1];
        } catch (IOException e)
        {
            return null;
        }
        return accessToken;
    }
}
