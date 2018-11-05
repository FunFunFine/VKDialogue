package ru.urgu.vkDialogueBot;

import ru.urgu.vkDialogueBot.Controller.BotController;
import ru.urgu.vkDialogueBot.Model.VkCommunityModel;
import ru.urgu.vkDialogueBot.View.ConsoleView.ConsoleView;

public class Main
{

    public static void main(String[] args)
    {
        var gui = new ConsoleView();
        var controller = new BotController(new VkCommunityModel(), gui);
        controller.runBot();
    }
}
