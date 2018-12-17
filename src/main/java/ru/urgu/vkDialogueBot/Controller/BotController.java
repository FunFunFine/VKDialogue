package ru.urgu.vkDialogueBot.Controller;

import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObservable;
import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObserver;
import ru.urgu.vkDialogueBot.Events.*;
import ru.urgu.vkDialogueBot.Model.IVkModel;
import ru.urgu.vkDialogueBot.View.IView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.Function;

public class BotController implements IObserver, IObservable
{
    private final IView _gui;
    private final IVkModel _model;
    private final UsersDataBase _users ;
    private Long _currentTelegramId = -1L;
    private LinkedList<IObserver> _observers = new LinkedList<>();
    private CommandParser _parser = null;
    private final Map<Class, Function<Signal, Signal>> _eventActionMapping = new HashMap<>()
    {
        {
            put(GUIStartedSignal.class, signal -> greetUser());
            put(GetHelpEvent.class, event -> processHelp((GetHelpEvent) event));
            put(GUIExitSignal.class, event -> event);
            put(SendMessageEvent.class, event -> processSend((SendMessageEvent) event));
            put(CheckMessagesEvent.class, event -> processCheck((CheckMessagesEvent) event));
            put(FailureEvent.class, event -> event);
            put(SetUserEvent.class, event -> processSet((SetUserEvent) event));
        }
    };


    public BotController(IVkModel vkModel, IView gui, UsersDataBase users)
    {
        _users = users;
        _eventActionMapping.put(UserIOSignal.class, signal -> {
            var ioSignal = ((UserIOSignal) signal);
            var parsedSignal = _parser.parse(ioSignal, _users.GetUser(_currentTelegramId));
            return _eventActionMapping.get(parsedSignal.getClass()).apply(parsedSignal);
        });
//        Set<Command> _commands = new HashSet<>()
//        {
//            {
//                add(new Command("отправить", (fields, i) -> SendMessageCommand(fields, i)));
//                add(new Command("прочитать", (fields, i) -> ReadMessagesCommand(fields, i)));
//            }
//        };
        _parser = new CommandParser();
        _model = vkModel;
        _gui = gui;
        gui.addObserver(this);
        this.addObserver(gui);

        //_user = new SimpleUserToken(5463728);
    }

    private Signal processHelp(GetHelpEvent event)
    {
        final UserIOSignal userIOSignal = new UserIOSignal(event.getMessage());
        userIOSignal.setTelegramId(_currentTelegramId);

        return userIOSignal;
    }

    private Signal processSet(SetUserEvent event)
    {
        _users.GetUser(_currentTelegramId).setCurrentResponderId(event.getId());
        final UserIOSignal userIOSignal = new UserIOSignal("Готово!");
        userIOSignal.setTelegramId(_currentTelegramId);

        return userIOSignal;
    }


    public void runBot()
    {
        _gui.run();
    }

    private Signal greetUser()
    {
        final UserIOSignal userIOSignal = new UserIOSignal("Привет! Я  - Телеграмматор. Команда \"помощь\" расскажет про меня подробнее :)");
        userIOSignal.setTelegramId(_currentTelegramId);
        return userIOSignal;
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
            final UserIOSignal signal1 = new UserIOSignal("Отправлено");
            signal1.setTelegramId(_currentTelegramId);
            responseSignal = signal1;
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
            final UserIOSignal responseSignal1 = new UserIOSignal(messages);
            responseSignal1.setTelegramId(_currentTelegramId);
            responseSignal = responseSignal1;
        }
        return (responseSignal);
    }


    @Override
    public void receive(Signal signal)
    {
        _currentTelegramId = signal.getTelegramId();
        _users.AddUser(_currentTelegramId);
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
