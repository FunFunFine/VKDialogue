package ru.urgu.vkDialogueBot.Controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObserver;
import ru.urgu.vkDialogueBot.Events.CheckMessagesEvent;
import ru.urgu.vkDialogueBot.Events.SendMessageEvent;
import ru.urgu.vkDialogueBot.Events.Signal;
import ru.urgu.vkDialogueBot.Events.UserIOSignal;
import ru.urgu.vkDialogueBot.Model.IVkModel;
import ru.urgu.vkDialogueBot.View.IView;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BotController_Should
{
    private IView fakeView;
    private IVkModel fakeModel;
    private BotController controller;
    private UsersDataBase dataBase;

    @Before
    public void setUp()
    {
        fakeView = Mockito.mock(IView.class);
        fakeModel = mock(IVkModel.class);
        dataBase = new UsersDataBase();
        controller = new BotController(fakeModel, fakeView, dataBase);
    }

    @Test
    public void Run_Gui()
    {
        controller.runBot();
        verify(fakeView).run();
    }

    @Test
    public void SendMessages()
    {
        var setSignal = new UserIOSignal("выбрать_получателя 1");
        var signal = new UserIOSignal("отправить 1234");
        controller.receive(setSignal);
        controller.receive(signal);
        verify(fakeModel).sendMessage(any(SendMessageEvent.class));
        verify(fakeView, times(2)).receive(any(UserIOSignal.class));
    }

    @Test
    public void SetUser()
    {
        var signal = new UserIOSignal("выбрать_получателя 42");
        controller.receive(signal);
        var currentId = signal.getTelegramId();
        assertEquals(42, dataBase.GetUser(currentId).getCurrentResponderId());
        verify(fakeView).receive(any(UserIOSignal.class));
    }

    @Test
    public void ReadMessages()
    {
        var setSignal = new UserIOSignal("выбрать_получателя 1");
        var signal = new UserIOSignal("прочитать");
        controller.receive(setSignal);
        try
        {
            controller.receive(signal);
        } catch (Exception ignored)
        {
        } finally
        {
            verify(fakeModel).checkMessages(any(CheckMessagesEvent.class));
        }
    }

    @Test
    public void Notify_AllObservers()
    {
        var observers = new ArrayList<IObserver>();
        for (var i = 0; i < 42; i++)
        {
            var observer = mock(IObserver.class);
            observers.add(observer);
            controller.addObserver(observer);
        }

        controller.notify(new UserIOSignal("smth"));

        for (var obs : observers)
        {
            verify(obs).receive(any(Signal.class));
        }
        verify(fakeView).receive(any(Signal.class));
    }

    @Test
    public void ReturnHelp()
    {
        var signal = new UserIOSignal("помощь");
        controller.receive(signal);
        verify(fakeView).receive(any(UserIOSignal.class));
    }

}
