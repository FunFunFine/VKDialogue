package ru.urgu.vkDialogueBot.Model;

import lombok.Getter;
import ru.urgu.vkDialogueBot.Events.Signal;
import ru.urgu.vkDialogueBot.Utils.Func;

public class Command
{
    @Getter
    private String _name;


    @Getter
    private Func<String[], Signal> _handler;

    public Command(String name, Func<String[], Signal> handler)
    {
        _name = name.toLowerCase().trim();
        _handler = handler;
    }


}
