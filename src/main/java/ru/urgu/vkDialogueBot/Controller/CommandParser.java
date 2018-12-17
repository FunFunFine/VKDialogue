package ru.urgu.vkDialogueBot.Controller;

import lombok.var;
import ru.urgu.vkDialogueBot.Events.*;
import ru.urgu.vkDialogueBot.Utils.Func;

import java.util.HashMap;
import java.util.Map;

public class CommandParser
{
    private Map<String, Func<String[], Signal>> _commands = new HashMap<>();

    public CommandParser()
    {
        addCommand(new Command("help", this::ShowHelpCommand));
        addCommand(new Command("set", this::SetUserCommand));
        addCommand(new Command("exit", this::ExitCommand));
        addCommand(new Command("help", this::ShowHelpCommand));
    }

    private Signal ExitCommand(String[] args)
    {
        return new GUIExitSignal();
    }

    private Signal SetUserCommand(String[] args)
    {
        var id = 0;
        try
        {
            id = Integer.parseInt(args[0]);
        } catch (Exception e)
        {
            return new FailureEvent(null, "Неверный id");
        }
        return new SetUserEvent(null, id);
    }

    private Signal ShowHelpCommand(String[] args)
    {
        var message = "";
        if (args.length != 0)
        {
            message = "Неизвестная команда";
        }
        else
        {
            message = "send *message* - отправить сообщение пользователю в текущий диалог\n" +
                    "set *id* - переключиться на диалог с пользователем *id*\n" +
                    "read - прочитать все новые + 10 старых сообщений из текущего диалога\n" +
                    "funfunfine.github.io - здесь можно разрешить нам писать сообщения вам ВК\n" +
                    "exit - выход\n";
        }
        return new GetHelpEvent(null, message);
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
