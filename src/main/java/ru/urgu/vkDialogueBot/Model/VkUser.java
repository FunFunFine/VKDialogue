package ru.urgu.vkDialogueBot.Model;

import lombok.Value;
import ru.urgu.vkDialogueBot.Controller.IUser;
import ru.urgu.vkDialogueBot.Controller.IUserToken;

@Value
class VkUser implements IUser
{
    private final IUserToken _token;

    VkUser(IUserToken userToken)
    {
        _token = userToken;
    }
}
