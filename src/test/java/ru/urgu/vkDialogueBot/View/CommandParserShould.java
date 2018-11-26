package ru.urgu.vkDialogueBot.View;

import org.junit.Before;
import org.junit.Test;
import ru.urgu.vkDialogueBot.Controller.Command;
import ru.urgu.vkDialogueBot.Controller.CommandParser;
import ru.urgu.vkDialogueBot.Events.Event;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class CommandParserShould
{
    private CommandParser parser;
    private Set<Command> _commands = new HashSet<>()
    {
        {
            add(new Command("help", this::ensure));
            add(new Command("set", this::ensure));
            add(new Command("send", this::ensure));
            add(new Command("read", this::ensure));
            add(new Command("exit", this::ensure));
        }

        private Event ensure(String[] fields)
        {
            assertTrue(true);
            return null;
        }

    };


    @Before
    public void setUp()
    {
        //parser = new CommandParser(_commands.toArray(new Command[0]));
    }


    @Test
    public void Pass_WhenHelp()
    {
        parser.parse("Help");
        parser.parse("help");
    }

    @Test
    public void Pass_WhenSet()
    {
        parser.parse("set 12434");
        parser.parse("Set 1243345");
    }

    @Test
    public void Pass_WhenRead()
    {
        parser.parse("read");
        parser.parse("READ");
    }

    @Test
    public void Pass_WhenSend()
    {
        parser.parse("Send abwer");
        parser.parse("send 1234");
    }
}