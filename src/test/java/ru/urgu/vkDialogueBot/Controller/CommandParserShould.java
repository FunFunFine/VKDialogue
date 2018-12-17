package ru.urgu.vkDialogueBot.Controller;

import org.junit.Test;
import ru.urgu.vkDialogueBot.Events.*;

import static org.junit.Assert.assertSame;

public class CommandParserShould
{
    private CommandParser parser = new CommandParser();

    @Test
    public void Pass_WhenHelp()
    {
        var sigBig = new UserIOSignal("Помощь");
        var sigSmall = new UserIOSignal("помощь");

        CheckResult(GetHelpEvent.class, sigBig, sigSmall);
    }

    @Test
    public void Pass_WhenSet()
    {
        var sigBig = new UserIOSignal("выбрать_получателя 12434");
        var sigSmall = new UserIOSignal("Выбрать_получателя 1243345");

        CheckResult(SetUserEvent.class, sigBig, sigSmall);
    }


    @Test
    public void Pass_WhenRead()
    {
        var sigBig = new UserIOSignal("прочитать");
        var sigSmall = new UserIOSignal("ПРОЧитать");

        CheckResult(CheckMessagesEvent.class, sigBig, sigSmall);
    }

    @Test
    public void Pass_WhenSend()
    {
        var sigBig = new UserIOSignal("Отправить abwer");
        var sigSmall = new UserIOSignal("отправить 1234");

        CheckResult(SendMessageEvent.class, sigBig, sigSmall);
    }

    @Test
    public void Pass_WhenExit()
    {
        var sigBig = new UserIOSignal("Выход");
        var sigSmall = new UserIOSignal("выход");

        CheckResult(GUIExitSignal.class, sigBig, sigSmall);
    }

    private void CheckResult(Class resultSignal, UserIOSignal... signals)
    {
        var responder = new SimpleUserToken(0l);
        responder.setCurrentResponderId(0);
        for (var signal : signals)
        {
            var result = parser.parse(signal, responder);
            assertSame(resultSignal, result.getClass());
        }
    }
}