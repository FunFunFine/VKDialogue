package ru.urgu.vkDialogueBot.GUI;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class GUI
{
    private PropertyChangeSupport support;

    public GUI()
    {
        support = new PropertyChangeSupport(this);

    }
    public void run()
    {
        while (true)
        {
            //run interface
            // if (event)
            //onEvent(event);
        }

    }

    public void addPropertyChangeListener(PropertyChangeListener pcl)
    {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl)
    {
        support.removePropertyChangeListener(pcl);
    }

    private void onEvent()
    {
        support.firePropertyChange("Event", 1, 2);

    }

}
