package ru.urgu.vkDialogueBot.View;

import org.junit.Before;
import org.junit.Test;
import ru.urgu.vkDialogueBot.Controller.Command;
import ru.urgu.vkDialogueBot.Controller.CommandParser;
import ru.urgu.vkDialogueBot.Events.*;

import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CommandParserShould
{
    private CommandParser parser;
    private Set<Command> _commands = new HashSet<>()
    {
        {
            add(new Command("отправить", this::ensure));
            add(new Command("прочитать", this::ensure));
        }

        private Signal ensure(String[] fields, Long num)
        {
            assertTrue(true);
            return null;
        }

    };


    @Before
    public void setUp()
    {
        parser = new CommandParser(_commands.toArray(new Command[0]));
    }


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

    private void CheckResult(Class resultSignal, UserIOSignal... signals)
    {
        for (var signal : signals)
        {
            var result = parser.parse(signal);
            assertSame(resultSignal, result.getClass());
        }
    }

    @Test
    public void Pass_WhenRead()
    {
       // parser.parse("read");
       // parser.parse("READ");
    }

    @Test
    public void Pass_WhenSend()
    {
//        parser.parse("Send abwer");
//        parser.parse("send 1234");
    }
}