package ru.urgu.vkDialogueBot.Controller;

import ru.urgu.vkDialogueBot.GUI.GUI;
import ru.urgu.vkDialogueBot.Model.Model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Controller implements PropertyChangeListener
{
    private final GUI _gui;
    private final Model _model;
    private final IMessenger _messenger;


    public Controller(Model model, GUI gui)
    {
        _model = model;
        _messenger = new Messenger();
        _gui = gui;
        _gui.addPropertyChangeListener(this);
    }

    public void startBot()
    {
        _gui.run();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        var event = evt.getNewValue();
        // do smth with event
    }
}
