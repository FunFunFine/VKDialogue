package ru.urgu.vkDialogueBot.View;

import lombok.Getter;
import ru.urgu.vkDialogueBot.Utils.Action;

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
    private Action<String[]> _handler;

    public Command(String name, int fieldsCount, int optionalFieldsCount, Action<String[]> handler)
    {
        _name = name.toLowerCase().trim();
//        _fieldsCount = fieldsCount;
//        _optionalFieldsCount = optionalFieldsCount;
        _handler = handler;
    }


}
