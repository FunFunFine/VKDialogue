package ru.urgu.vkDialogueBot.View;

import lombok.Getter;
import ru.urgu.vkDialogueBot.Events.Event;
import ru.urgu.vkDialogueBot.Utils.Func;

public class Command
{
    @Getter
    private String _name;

//    @Getter
//    private int _fieldsCount;
//
//    @Getter
//    private int _optionalFieldsCount;

    @Getter
    private Func<String[], Event> _handler;

    public Command(String name, int fieldsCount, int optionalFieldsCount, Func<String[], Event> handler)
    {
        _name = name.toLowerCase().trim();
//        _fieldsCount = fieldsCount;
//        _optionalFieldsCount = optionalFieldsCount;
        _handler = handler;
    }


}
