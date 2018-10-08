package ru.urgu.vkDialogueBot.Controller;

import ru.urgu.vkDialogueBot.GUI.GUI;
import ru.urgu.vkDialogueBot.Model.Model;

public class Controller
{
    private final GUI _gui;
    private final Model _model;
    private final IMessenger _messenger;
    private final EventManager _eventManager;

    public Controller(Model model, GUI gui)
    {
        _model = model;
        _messenger = new Messenger();

        _eventManager = new EventManager(_messenger);
        _gui = gui;
    }

    public void startBot()
    {
        _gui.run();
    }
}
