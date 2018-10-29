package ru.urgu.vkDialogueBot.Utils;

public interface Action<T>
{
    void apply(T param);
}
