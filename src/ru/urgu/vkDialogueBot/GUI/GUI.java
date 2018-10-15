package ru.urgu.vkDialogueBot.GUI;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Method;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GUI
{
    private final PropertyChangeSupport _support;
    private GUIState _state = GUIState.UNAUTHORISED;
    private final Map<GUIState, String> _state_menus = new HashMap<GUIState, String>()
    {{
        put(GUIState.UNAUTHORISED, "Введите логин и пароль:");
        put(GUIState.NOTREAD, "1. Проверить сообщения\n2. Выйти");
        put(GUIState.READ, "1. Проверить сообщения\n2. Открыть диалог с юзером\n3. Выйти");
        put(GUIState.INDIALOGUE, "");
    }};

    private void executeStateMethod(GUIState state)
    {
        if (state == GUIState.UNAUTHORISED)
            authorise();
    }

    private void authorise()
    {
        var input = new Scanner(System.in);
        System.out.println("Логин: ");
        var login = input.nextLine();
        System.out.println("Пароль: ");
        var password = input.nextLine();
        onEvent(new UserCreationEvent(login, password));
    }

    public GUI()
    {
        _support = new PropertyChangeSupport(this);
    }

    public void run()
    {

        while (true)
        {
            System.out.println(_state_menus.get(_state));
            executeStateMethod(_state);
        }
    }



    public void addPropertyChangeListener(PropertyChangeListener pcl)
    {
        _support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl)
    {
        _support.removePropertyChangeListener(pcl);
    }

    private void onEvent(IEvent event)
    {
        _support.firePropertyChange("Event", 1, event);
    }
}
