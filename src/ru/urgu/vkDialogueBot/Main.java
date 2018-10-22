package ru.urgu.vkDialogueBot;

import ru.urgu.vkDialogueBot.Controller.Controller;
import ru.urgu.vkDialogueBot.GUI.GUI;
import ru.urgu.vkDialogueBot.Model.Model;

public class Main
{

    public static void main(String[] args)
    {
        var gui = new GUI();
        var model = new Model();
        var controller = new Controller(model, gui);
        controller.startBot();
        // write your code here
    }
}
