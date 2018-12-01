package ru.urgu.vkDialogueBot.Controller;

import ru.urgu.vkDialogueBot.Events.*;
import ru.urgu.vkDialogueBot.Utils.FuncDouble;

import java.util.HashMap;
import java.util.Map;

public class CommandParser
{
    private Map<String, FuncDouble<String[], Long, Signal>> _commands = new HashMap<>();

    public CommandParser()
    {
        addCommand(new Command("помощь", this::ShowHelpCommand));
        addCommand(new Command("выбрать_получателя", this::SetUserCommand));
        addCommand(new Command("выход", this::ExitCommand));
    }

    private Signal ExitCommand(String[] args, Long id)
    {

        final GUIExitSignal guiExitSignal = new GUIExitSignal();
        guiExitSignal.setTelegramId(id);
        return guiExitSignal;
    }

    private Signal SetUserCommand(String[] args, Long chatId)
    {
        var id = 0;
        try
        {
            id = Integer.parseInt(args[0]);
        } catch (Exception e)
        {
            final FailureEvent event = new FailureEvent(null, "Неверный id");
            event.setTelegramId(chatId);
            return event;
        }
        return new SetUserEvent(null, id);
    }

    private Signal ShowHelpCommand(String[] args, Long chatId)
    {
        var message = "";
        message = "отправить *сообщение* - отправить сообщение пользователю в текущий диалог\n" +
                "выбрать_получателя *id* - переключиться на диалог с пользователем *id*\n" +
                "прочитать - прочитать все новые + 10 старых сообщений из текущего диалога\n" +
                "funfunfine.github.io - здесь можно разрешить нам писать сообщения вам ВК\n" +
                "выход - выход\n";
        final GetHelpEvent getHelpEvent = new GetHelpEvent(null, message);
        getHelpEvent.setTelegramId(chatId);
        return getHelpEvent;
    }


    void addCommand(Command command)
    {
        _commands.put(command.getName(), command.getHandler());
    }


    Signal parse(UserIOSignal signal)
    {
        var command = signal.getText();
        var fields = command.toLowerCase().trim().split(" ");
        if (fields.length == 0)
        {
            final FailureEvent failureEvent = new FailureEvent(null, "Вы ничего не напечатали");
            failureEvent.setTelegramId(signal.getTelegramId());
            return failureEvent;
        }
        var commandName = fields[0];
        if (!_commands.containsKey(commandName))
        {
            final FailureEvent failureEvent = new FailureEvent(null, "Неизвестная команда");
            failureEvent.setTelegramId(signal.getTelegramId());
            return failureEvent;
        }
        var arguments = new String[fields.length - 1];
        System.arraycopy(fields, 1, arguments, 0, fields.length - 1);

        return _commands.get(commandName).apply(arguments, signal.getTelegramId());
    }
}
