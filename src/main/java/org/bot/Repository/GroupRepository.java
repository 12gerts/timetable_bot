package org.bot.Repository;

import java.util.HashMap;

public class GroupRepository {
    private final HashMap<String, String> chatIdToGroupNumber = new HashMap<>();

    public String getGroupNumber(String chatId) {
        return chatIdToGroupNumber.get(chatId);
    }

    public void setGroupNumber(String chatId, String groupNumber) {
        if (chatIdToGroupNumber.containsKey(chatId)) {
            chatIdToGroupNumber.replace(chatId, groupNumber);
        } else {
            chatIdToGroupNumber.put(chatId, groupNumber);
        }
    }

    public void setGroupNumberIfNotContains(String chatId, String groupNumber) {
        if (!chatIdToGroupNumber.containsKey(chatId)) {
            chatIdToGroupNumber.put(chatId, groupNumber);
        }
    }
}
