package org.bot;

public interface IGroup {
    String convertAndUpdateNumberOfGroup(String group, String chatId);
    String checkGroupChange(String chatId);
}
