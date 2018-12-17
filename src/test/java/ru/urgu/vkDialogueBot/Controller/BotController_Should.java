package ru.urgu.vkDialogueBot.Controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import ru.urgu.vkDialogueBot.Controller.ObserverPattern.IObserver;
import ru.urgu.vkDialogueBot.Events.Signal;
import ru.urgu.vkDialogueBot.Events.UserIOSignal;
import ru.urgu.vkDialogueBot.Model.IVkModel;
import ru.urgu.vkDialogueBot.View.IView;

import static org.mockito.Mockito.*;
import org.junit.Test;

@RunWith(MockitoJUnitRunner.class)
public class BotController_Should
{
    IView fakeView;
    IVkModel fakeModel;
    BotController controller;


    @Before
    public void setUp()
    {
        fakeView = Mockito.mock(IView.class);
        fakeModel = mock(IVkModel.class);
        var dataBase = new UsersDataBase();
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
        var signal = new UserIOSignal("отправить 1234");
        //controller.
    }

}
