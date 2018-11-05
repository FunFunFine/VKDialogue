package ru.urgu.vkDialogueBot.Controller;

import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObservable;
import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObserver;
import ru.urgu.vkDialogueBot.Events.CheckMessagesEvent;
import ru.urgu.vkDialogueBot.Events.FailureEvent;
import ru.urgu.vkDialogueBot.Events.GetHelpEvent;
import ru.urgu.vkDialogueBot.Events.SendMessageEvent;
import ru.urgu.vkDialogueBot.Model.VkCommunityModel;
import ru.urgu.vkDialogueBot.View.IView;

import java.util.*;
import java.util.function.Function;

public class BotController implements IObserver, IObservable
{
    private final IView _gui;
    private final VkCommunityModel _model;
    private LinkedList<IObserver> _observers = new LinkedList<>();
    private final SimpleUserToken _user;
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
                add(new Command("help", fields -> ShowHelp(fields)));
                add(new Command("set", fields -> SetUser(fields)));
                add(new Command("send", fields -> SendMessage(fields)));
                add(new Command("read", fields -> ReadMessages(fields)));
                add(new Command("exit", fields -> Exit()));
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

        _user = new SimpleUserToken(5463728);
    }

    private Signal Exit()
    {
        return new GUIExitSignal();
    }

    private Signal ReadMessages(String[] args)
    {
        if (args.length != 0)
        {
            return new FailureEvent(null, "Неизвестная команда");
        }
        if (_user.getCurrentResponderId() == -1)
        {
            return new FailureEvent(null, "Нужно сделать set *id*");
        }
        var event = new CheckMessagesEvent(_user.getCurrentResponderId(), _user);
        event.setOldMessagesAmount(10);
        return event;
    }

    private Signal SendMessage(String[] fields)
    {
        if (fields.length == 0)
        {
            return new FailureEvent(null, "Зачем посылать пустое сообщение?");
        }
        if (_user.getCurrentResponderId() == -1)
        {
            return new FailureEvent(null, "Нужно сделать set *id*");
        }
        var headline = "Сообщение пользователя " + _user.getHash() + ":\n";
        var messageBuilder = new StringBuilder();
        for (String field : fields)
        {
            messageBuilder.append(field).append(" ");
        }
        return new SendMessageEvent(_user.getCurrentResponderId(), headline + messageBuilder.toString(), _user);
    }

    private Signal ShowHelp(String[] args)
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

    private Signal processHelp(GetHelpEvent event)
    {
        return new UserIOSignal(event.getMessage());
    }

    private Signal processSet(SetUserEvent event)
    {
        _user.setCurrentResponderId(event.getId());
        return new UserIOSignal("Готово!");
    }

    private Signal SetUser(String[] args)
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
        UserIOSignal responseSignal;
        if (response instanceof FailureEvent)
        {
            responseSignal = new UserIOSignal(response.describe());
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
        UserIOSignal responseSignal;
        if (response instanceof FailureEvent)
        {
            responseSignal = new UserIOSignal(response.describe());
        }
        else
        {
            var builder = new StringBuilder();
            for (var s : ((CheckMessagesEvent) response).getMessages())
            {
                builder.append(s);
                builder.append("\n");
            }
            responseSignal = new UserIOSignal(builder.toString());
        }
        return (responseSignal);
    }


    @Override
    public void receive(Signal signal)
    {
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
