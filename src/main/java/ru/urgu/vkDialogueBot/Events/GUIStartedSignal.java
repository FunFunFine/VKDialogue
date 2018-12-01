package ru.urgu.vkDialogueBot.Events;

public class GUIStartedSignal implements Signal
{
    private Long chatId;
    @Override
    public Long getTelegramId()
    {
        return chatId;
    }

    @Override
    public void setTelegramId(Long chatId)
    {
        this.chatId = chatId;
    }


}