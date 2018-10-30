package ru.urgu.vkDialogueBot.View;

import ru.urgu.vkDialogueBot.Events.Event;
import ru.urgu.vkDialogueBot.Utils.Func;

import java.util.HashMap;
import java.util.Map;

public class CommandParser
{
    private Map<String, Func<String[], Event>> _commands = new HashMap<>();

    public CommandParser(Command[] commands)
    {
        for (var command : commands)
        {
            addCommand(command);
        }
    }

    private CommandParser()
    {
    }

    public void addCommand(Command command)
    {
        _commands.put(command.getName(), command.getHandler());
    }

    public Event parse(String command)
    {
        var fields = command.toLowerCase().trim().split(" ");
        if (fields.length == 0)
        {
            // System.out.println("Вы ничего не напечатали");
            throw new UnsupportedOperationException("Вы ничего не напечатали");
        }
        var commandName = fields[0];
        if (!_commands.containsKey(commandName))
        {
            //System.out.println("Неизвестная команда");
            throw new UnsupportedOperationException("Неизвестная команда");
        }
        var arguments = new String[fields.length - 1];
        System.arraycopy(fields, 1, arguments, 0, fields.length - 1);
        return _commands.get(commandName).apply(arguments);
    }
}
