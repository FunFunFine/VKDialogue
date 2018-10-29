package ru.urgu.vkDialogueBot.View;

import ru.urgu.vkDialogueBot.Events.Event;
import ru.urgu.vkDialogueBot.Utils.Action;

import java.util.*;

public class CommandParser
{
    private Map<String, Action<String[]>> _commands;

    public CommandParser(Command[] commands)
    {
        _commands = new HashMap<>();
        for (var command: commands)
            _commands.put(command.getName(), command.getHandler());
    }

    public Event parse(String command)
    {
        var fields = command.toLowerCase().trim().split(" ");
        if (fields.length == 0)
        {
            System.out.println("Вы ничего не напечатали");
            return null;
        }
        var commandName = fields[0];
        if (_commands.containsKey(commandName))
        {
            System.out.println("Неизвестная команда");
            return null;
        }
        return null;

    }
}
