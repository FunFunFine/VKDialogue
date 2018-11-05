package ru.urgu.vkDialogueBot.Controller;

import ru.urgu.vkDialogueBot.Events.FailureEvent;
import ru.urgu.vkDialogueBot.Events.Signal;
import ru.urgu.vkDialogueBot.Utils.Func;

import java.util.HashMap;
import java.util.Map;

public class CommandParser
{
    private Map<String, Func<String[], Signal>> _commands = new HashMap<>();

    public CommandParser(Command[] commands)
    {
        for (var command : commands)
        {
            addCommand(command);
        }
    }

    public CommandParser()
    {
    }

    public void addCommand(Command command)
    {
        _commands.put(command.getName(), command.getHandler());
    }

    public Signal parse(String command)
    {
        var fields = command.toLowerCase().trim().split(" ");
        if (fields.length == 0)
        {
            return new FailureEvent(null, "Вы ничего не напечатали");
        }
        var commandName = fields[0];
        if (!_commands.containsKey(commandName))
        {
            return new FailureEvent(null, "Неизвестная команда");
        }
        var arguments = new String[fields.length - 1];
        System.arraycopy(fields, 1, arguments, 0, fields.length - 1);
        return _commands.get(commandName).apply(arguments);
    }
}
