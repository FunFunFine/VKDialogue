package ru.urgu.vkDialogueBot.View;

public class Logger
{
    public void Log(String message)
    {
        System.out.println("[LOG]\t"+message);
    }
    public void Error(String error)
    {
        System.err.println("[ERROR]\t"+error);
    }
}
