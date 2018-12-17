package ru.urgu.vkDialogueBot.Controller;

import lombok.Getter;
import ru.urgu.vkDialogueBot.Events.Signal;
import ru.urgu.vkDialogueBot.Utils.Func;

import java.util.Map;

public class Command
{
    @Getter
    private String _name;


    @Getter
    private Func<Map<CommandArguments, Object>, Signal> _handler;

    public Command(String name, Func<Map<CommandArguments, Object>, Signal> handler)
    {
        _name = name.toLowerCase().trim();
        _handler = handler;
    }


}
