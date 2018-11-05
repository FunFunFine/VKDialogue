package ru.urgu.vkDialogueBot.View.ConsoleView;

import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObserver;
import ru.urgu.vkDialogueBot.Events.*;
import ru.urgu.vkDialogueBot.View.IView;

import java.util.*;

public class ConsoleView implements IView
{
    // Антон - send 147985909
    // Саша - send 161856178
    private final List<IObserver> _observers = new LinkedList<>();
    //private final SimpleUserToken _user;
    private ConsoleViewState _currentState = ConsoleViewState.Offline;
//    private Map<Class, Action<Event>> _eventProcessMapping = new HashMap<>()
//    {
//        {
//            put(SendMessageEvent.class, event -> processSendMessages((SendMessageEvent) event));
//            put(CheckMessagesEvent.class, event -> processCheckMessages((CheckMessagesEvent) event));
//            put(FailureEvent.class, event -> processFailure((FailureEvent) event));
//        }
//    };
//    private Set<Command> _commands = new HashSet<>()
//    {
//        {
//            add(new Command("help", fields -> ShowHelp(fields)));
//            add(new Command("set", fields -> SetUser(fields)));
//            add(new Command("send", fields -> SendMessage(fields)));
//            add(new Command("read", fields -> ReadMessages(fields)));
//            add(new Command("exit", fields -> Exit(fields)));
//        }
//    };
//
//    public ConsoleView()
//    {
//        _user = new SimpleUserToken(5463728);
//    }
//
//    private void processCheckMessages(CheckMessagesEvent event)
//    {
//        var messages = event.getMessages();
//        for (var message : messages)
//        {
//            System.out.println(message);
//        }
//    }
//
//    private void processSendMessages(SendMessageEvent event)
//    {
//        System.out.println("Message sent");
//    }
//
    private String readCommand(Scanner scanner)
    {
        System.out.println("Жду команду...");
        return scanner.nextLine();
    }

    private void writeText(String text)
    {
        System.out.println(text);
    }
//
//    private Event Exit(String[] args)
//    {
//        System.out.println("До свидания");
//        _currentState = ConsoleViewState.Offline;
//        return null;
//    }
//
//    private Event ReadMessages(String[] args)
//    {
//        if (args.length != 0)
//        {
//            System.out.println("Неизвестная команда");
//            return null;
//        }
//        if (_user.getCurrentResponderId() == -1)
//        {
//            System.out.println("Нужно сделать set *id*");
//            return null;
//        }
//        var event = new CheckMessagesEvent(_user.getCurrentResponderId(), _user);
//        event.setOldMessagesAmount(10);
//        return event;
//    }
//
//    private Event SendMessage(String[] fields)
//    {
//        if (fields.length == 0)
//        {
//            System.out.println("Зачем посылать пустое сообщение?");
//            return null;
//        }
//        if (_user.getCurrentResponderId() == -1)
//        {
//            System.out.println("Нужно сделать set *id*");
//            return null;
//        }
//        var headline = "Сообщение пользователя " + _user.getHash() + ":\n";
//        var messageBuilder = new StringBuilder();
//        for (String field : fields) {
//            messageBuilder.append(field).append(" ");
//        }
//        return new SendMessageEvent(_user.getCurrentResponderId(), headline + messageBuilder.toString(), _user);
//    }
//
//    private Event SetUser(String[] args)
//    {
//        var id = 0;
//        try
//        {
//            id = Integer.parseInt(args[0]);
//        } catch (Exception e)
//        {
//            System.out.println("Неверный id");
//            return null;
//        }
//        _user.setCurrentResponderId(id);
//        return null;
//    }
//
////    private Event ShowHelp(String[] args)
////    {
////        if (args.length != 0)
////        {
////            System.out.println("Неизвестная команда");
////            return null;
////        }
////        System.out.println("send *message* - отправить сообщение пользователю в текущий диалог");
////        System.out.println("create *id* - создать диалог с пользователем *id*");
////        System.out.println("set *id* - переключиться на диалог с пользователем *id*");
////        System.out.println("read - прочитать все новые + 10 старых сообщений из текущего диалога");
////        System.out.println("funfunfine.github.io - здесь можно разрешить нам писать сообщения вам ВК");
////        System.out.println("exit - выход");
////        return null;
////    }

    @Override
    public void run()
    {
        _currentState = ConsoleViewState.Started;
//        var parser = new CommandParser(_commands.toArray(new Command[0]));
        var scanner = new Scanner(System.in);
        while (!_currentState.equals(ConsoleViewState.Offline))
        {
            switch (_currentState)
            {
                case Working:
                    var msg = readCommand(scanner);
                    notify(new UserIOSignal(msg));
                case Offline:
                    break;
                case Started:
                    notify(new GUIStartedSignal());
                    _currentState = ConsoleViewState.Working;
                    break;
            }
//            Event event;
//            try
//            {
//                event = parser.parse(readCommand(scanner));
//            } catch (UnsupportedOperationException e)
//            {
//                System.out.println("неизвестная команда");
//                continue;
//            }
//            if (event != null)
//            {
//                notify(event);
//            }
        }
    }

    private void greetUser()
    {
        System.out.println("Привет! Я  - Телеграмматор. Команда \"Help\" расскажет про меня подробнее :)");
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

    @Override
    public void receive(Signal event)
    {
        if (event instanceof UserIOSignal)
            writeText(event.getText());
    }

    private void processFailure(FailureEvent event)
    {
        System.out.println(event.getUserToken() + event.describe());
    }
}