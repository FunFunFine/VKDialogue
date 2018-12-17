package ru.urgu.vkDialogueBot.Controller;

import ru.urgu.vkDialogueBot.Events.*;
import ru.urgu.vkDialogueBot.Utils.Func;

import java.util.HashMap;
import java.util.Map;

public class CommandParser
{
    private Map<String, Func<Map<CommandArguments, Object>, Signal>> _commands = new HashMap<>();
    private Map<CommandArguments, Object> _kwargs = new HashMap<>();

    CommandParser()
    {
        addCommand(new Command("помощь", this::ShowHelpCommand));
        addCommand(new Command("выбрать_получателя", this::SetUserCommand));
        addCommand(new Command("выход", this::ExitCommand));
        addCommand(new Command("отправить", this::SendMessageCommand));
        addCommand(new Command("прочитать", this::ReadMessagesCommand));
    }

    public CommandParser(Command[] commands)
    {
        this();
        for (var command : commands)
        {
            addCommand(command);
        }
    }

    private Signal ExitCommand(Map<CommandArguments, Object> kwargs)
    {
        var currentTelegramId = (Long) kwargs.get(CommandArguments.TelegramId);

        final GUIExitSignal guiExitSignal = new GUIExitSignal();
        guiExitSignal.setTelegramId(currentTelegramId);
        return guiExitSignal;
    }

    private Signal SetUserCommand(Map<CommandArguments, Object> kwargs)
    {
        var args = (String[]) kwargs.get(CommandArguments.UserArgs);
        var currentTelegramId = (Long) kwargs.get(CommandArguments.TelegramId);

        var id = 0;
        try
        {
            id = Integer.parseInt(args[0]);
        } catch (Exception e)
        {
            final FailureEvent event = new FailureEvent(null, "Неверный id");
            event.setTelegramId(currentTelegramId);
            return event;
        }
        final SetUserEvent setUserEvent = new SetUserEvent(null, id);
        setUserEvent.setTelegramId(currentTelegramId);
        return setUserEvent;
    }

    private Signal ShowHelpCommand(Map<CommandArguments, Object> kwargs)
    {
        var currentTelegramId = (Long) kwargs.get(CommandArguments.TelegramId);

        var message = "";
        message = "отправить *сообщение* - отправить сообщение пользователю в текущий диалог\n" +
                "выбрать_получателя *id* - переключиться на диалог с пользователем *id*\n" +
                "прочитать - прочитать все новые + 10 старых сообщений из текущего диалога\n" +
                "funfunfine.github.io - здесь можно разрешить нам писать сообщения вам ВК\n" +
                "выход - выход\n";
        final GetHelpEvent getHelpEvent = new GetHelpEvent(null, message);
        getHelpEvent.setTelegramId(currentTelegramId);
        return getHelpEvent;
    }

    private Signal ReadMessagesCommand(Map<CommandArguments, Object> kwargs)
    {
        // по хорошему следующие 3 строки надо траем всё проверить что можно так делать
        var user = (SimpleUserToken) kwargs.get(CommandArguments.User);
        var args = (String[]) kwargs.get(CommandArguments.UserArgs);
        var currentTelegramId = (Long) kwargs.get(CommandArguments.TelegramId);

        if (args.length != 0)
        {
            final FailureEvent event = new FailureEvent(null, "Неизвестная команда");
            event.setTelegramId(currentTelegramId);
            return event;
        }
        if (user.getCurrentResponderId() == -1)
        {
            final FailureEvent failureEvent = new FailureEvent(null, "Нужно сделать выбрать_получателя *id*");
            failureEvent.setTelegramId(currentTelegramId);
            return failureEvent;
        }
        var event = new CheckMessagesEvent(user.getCurrentResponderId(), user);
        event.setOldMessagesAmount(10);
        event.setTelegramId(currentTelegramId);
        return event;
    }

    private Signal SendMessageCommand(Map<CommandArguments, Object> kwargs)
    {
        // по хорошему следующие 3 строки надо траем всё проверить что можно так делать, и если нет вернуть fail
        var user = (SimpleUserToken) kwargs.get(CommandArguments.User);
        var args = (String[]) kwargs.get(CommandArguments.UserArgs);
        var currentTelegramId = (Long) kwargs.get(CommandArguments.TelegramId);

        if (args.length == 0)
        {
            final FailureEvent failureEvent = new FailureEvent(null, "Зачем посылать пустое сообщение?");
            failureEvent.setTelegramId(currentTelegramId);
            return failureEvent;
        }
        if (user.getCurrentResponderId() == -1)
        {
            final FailureEvent failureEvent = new FailureEvent(null, "Нужно сделать выбрать_получателя *id*");
            failureEvent.setTelegramId(currentTelegramId);
            return failureEvent;
        }
        var headline = "Сообщение пользователя " + user.getHash() + ":\n";
        var messageBuilder = new StringBuilder();
        for (String field : args)
        {
            messageBuilder.append(field).append(" ");
        }
        final SendMessageEvent sendMessageEvent = new SendMessageEvent(user.getCurrentResponderId(), headline + messageBuilder.toString(), user);
        sendMessageEvent.setTelegramId(currentTelegramId);

        return sendMessageEvent;
    }


    private void addCommand(Command command)
    {
        _commands.put(command.getName(), command.getHandler());
    }


    Signal parse(UserIOSignal signal, SimpleUserToken user)
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

        _kwargs.put(CommandArguments.UserArgs, arguments);
        _kwargs.put(CommandArguments.TelegramId, signal.getTelegramId());
        _kwargs.put(CommandArguments.User, user);

        return _commands.get(commandName).apply(_kwargs);
    }
}
