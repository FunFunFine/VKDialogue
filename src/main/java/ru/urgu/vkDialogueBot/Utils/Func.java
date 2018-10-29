package ru.urgu.vkDialogueBot.Utils;

public interface Func<TIn, TOut>
{
    TOut apply(TIn param);
}
