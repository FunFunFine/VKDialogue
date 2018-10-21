package ru.urgu.vkDialogueBot.Model;

public interface Func<TIn, TOut>
{
    TOut act(TIn arg);
}
