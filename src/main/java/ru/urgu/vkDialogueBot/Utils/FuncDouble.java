package ru.urgu.vkDialogueBot.Utils;

public interface FuncDouble<T1, T2, TOut>
{
    TOut apply(T1 param1, T2 param2);
}
