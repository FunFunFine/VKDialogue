package ru.urgu.vkDialogueBot.Controller;

import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObservable;
import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObserver;
import ru.urgu.vkDialogueBot.Events.*;
import ru.urgu.vkDialogueBot.Model.VkCommunityModel;
import ru.urgu.vkDialogueBot.View.IView;

import java.util.*;
import java.util.function.Function;

public class BotController implements IObserver, IObservable
{
    private final IView _gui;
    private final VkCommunityModel _model;
    private final Map<Long, SimpleUserToken> _users = new HashMap<>();
    private long _currentTelegramId = -1;
    private LinkedList<IObserver> _observers = new LinkedList<>();
    private CommandParser _parser = null;
    private final Map<Class, Function<Signal, Signal>> _eventActionMapping = new HashMap<>()
    {
        {
            put(GUIStartedSignal.class, signal -> greetUser());
            put(UserIOSignal.class, signal -> {
                var text = ((UserIOSignal) signal).getText();
                var parsedSignal = _parser.parse(text);
                return _eventActionMapping.get(parsedSignal.getClass()).apply(parsedSignal);
            });
            put(GetHelpEvent.class, event -> processHelp((GetHelpEvent) event));
            put(GUIExitSignal.class, event -> event);
            put(SendMessageEvent.class, (event) -> processSend((SendMessageEvent) event));
            put(CheckMessagesEvent.class, (event) -> processCheck((CheckMessagesEvent) event));
            put(FailureEvent.class, event -> event);
            put(SetUserEvent.class, event -> processSet((SetUserEvent) event));
        }
    };


    public BotController(VkCommunityModel vkModel, IView gui)
    {
        _parser = new CommandParser();
        Set<Command> _commands = new HashSet<>()
        {
            {
                add(new Command("send", fields -> SendMessageCommand(fields)));
                add(new Command("read", fields -> ReadMessagesCommand(fields)));
            }
        };
        for (var command : _commands)
        {
            _parser.addCommand(command);
        }
        _model = vkModel;
        _gui = gui;
        gui.addObserver(this);
        this.addObserver(gui);

        //_user = new SimpleUserToken(5463728);
    }


    private Signal ReadMessagesCommand(String[] args)
    {
        var user = _users.get(_currentTelegramId);
        if (args.length != 0)
        {
            return new FailureEvent(null, "Неизвестная команда");
        }
        if (user.getCurrentResponderId() == -1)
        {
            return new FailureEvent(null, "Нужно сделать set *id*");
        }
        var event = new CheckMessagesEvent(user.getCurrentResponderId(), user);
        event.setOldMessagesAmount(10);
        return event;
    }

    private Signal SendMessageCommand(String[] fields)
    {
        var user = _users.get(_currentTelegramId);
        if (fields.length == 0)
        {
            return new FailureEvent(null, "Зачем посылать пустое сообщение?");
        }
        if (user.getCurrentResponderId() == -1)
        {
            return new FailureEvent(null, "Нужно сделать set *id*");
        }
        var headline = "Сообщение пользователя " + user.getHash() + ":\n";
        var messageBuilder = new StringBuilder();
        for (String field : fields)
        {
            messageBuilder.append(field).append(" ");
        }
        return new SendMessageEvent(user.getCurrentResponderId(), headline + messageBuilder.toString(), user);
    }

    private Signal processHelp(GetHelpEvent event)
    {
        return new UserIOSignal(event.getMessage());
    }

    private Signal processSet(SetUserEvent event)
    {
        _users.get(_currentTelegramId).setCurrentResponderId(event.getId());
        return new UserIOSignal("Готово!");
    }


    public void runBot()
    {
        _gui.run();
    }

    private Signal greetUser()
    {
        return new UserIOSignal("Привет! Я  - Телеграмматор. Команда \"Help\" расскажет про меня подробнее :)");
    }

    private Signal processSend(SendMessageEvent signal)
    {
        var response = _model.sendMessage(signal);
        Signal responseSignal;
        if (response instanceof FailureEvent)
        {
            responseSignal = response;
        }
        else
        {
            responseSignal = new UserIOSignal("Отправлено");
        }
        return (responseSignal);
    }

    private Signal processCheck(CheckMessagesEvent signal)
    {
        var response = _model.checkMessages(signal);
        Signal responseSignal;
        if (response instanceof FailureEvent)
        {
            responseSignal = response;
        }
        else
        {
            var messages = String.join("\n", ((CheckMessagesEvent) response).getMessages());
            responseSignal = new UserIOSignal(messages);
        }
        return (responseSignal);
    }


    @Override
    public void receive(Signal signal)
    {
        _currentTelegramId = signal.getTelegramId();
        if (!_users.containsKey(_currentTelegramId))
            _users.put(_currentTelegramId, new SimpleUserToken((int)_currentTelegramId));
        var result = _eventActionMapping.get(signal.getClass()).apply(signal);
        notify(result);
    }

    @Override
    public void notify(Signal event)
    {
        for (var observer : _observers)
        {
            observer.receive(event);
        }
    }

    @Override
    public void addObserver(IObserver observer)
    {
        _observers.add(observer);
    }

    @Override
    public void removeObserver(IObserver observer)
    {
        _observers.remove(observer);
    }

}
