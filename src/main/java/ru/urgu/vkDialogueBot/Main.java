package ru.urgu.vkDialogueBot;

import ru.urgu.vkDialogueBot.Controller.BotController;
import ru.urgu.vkDialogueBot.Model.VkModel;
import ru.urgu.vkDialogueBot.View.ConsoleView.ConsoleView;

public class Main
{

    public static void main(String[] args)
    {
        var gui = new ConsoleView();
        var controller = new BotController(new VkModel(), gui);
        gui.addObserver(controller);
        controller.addObserver(gui);
        controller.runBot();
    }
}
