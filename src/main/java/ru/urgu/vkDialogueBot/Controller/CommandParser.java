package ru.urgu.vkDialogueBot.Controller;

import ru.urgu.vkDialogueBot.Events.*;
import ru.urgu.vkDialogueBot.Utils.Func;

import java.util.HashMap;
import java.util.Map;

public class CommandParser
{
    private Map<String, Func<String[], Signal>> _commands = new HashMap<>();

    public CommandParser()
    {
        addCommand(new Command("помощь", this::ShowHelpCommand));
        addCommand(new Command("выбрать_получателя", this::SetUserCommand));
        addCommand(new Command("выход", this::ExitCommand));
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
            message = "отправить *сообщение* - отправить сообщение пользователю в текущий диалог\n" +
                    "выбрать_получателя *id* - переключиться на диалог с пользователем *id*\n" +
                    "прочитать - прочитать все новые + 10 старых сообщений из текущего диалога\n" +
                    "funfunfine.github.io - здесь можно разрешить нам писать сообщения вам ВК\n" +
                    "выход - выход\n";
        }
        return new GetHelpEvent(null, message);
    }


    void addCommand(Command command)
    {
        _commands.put(command.getName(), command.getHandler());
    }


    Signal parse(String command)
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
