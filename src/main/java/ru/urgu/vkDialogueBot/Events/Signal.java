package ru.urgu.vkDialogueBot.Events;

public interface Signal
{
    Long getTelegramId();

    void setTelegramId(Long chatId);
}
