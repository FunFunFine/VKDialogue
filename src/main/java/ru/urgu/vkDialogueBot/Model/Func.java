package ru.urgu.vkDialogueBot.Model;

@FunctionalInterface
public interface Func<TIn, TOut>
{
    TOut act(TIn arg);
}
