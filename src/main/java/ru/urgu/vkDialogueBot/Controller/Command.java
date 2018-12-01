package ru.urgu.vkDialogueBot.Controller;

import lombok.Getter;
import ru.urgu.vkDialogueBot.Events.Signal;
import ru.urgu.vkDialogueBot.Utils.FuncDouble;

public class Command
{
    @Getter
    private String _name;


    @Getter
    private FuncDouble<String[], Long, Signal> _handler;

    Command(String name, FuncDouble<String[], Long, Signal> handler)
    {
        _name = name.toLowerCase().trim();
        _handler = handler;
    }


}
