package org.bot;

import org.bot.Http.IHttpRequest;
import org.bot.Parser.IParserJson;
import org.bot.Repository.GroupRepository;


public class Group implements IGroup {
    private final IHttpRequest request;
    private final IParserJson parserJson;

    private final GroupRepository groupRepository;

    public Group(IHttpRequest request, IParserJson parser, GroupRepository groupRepository) {
        this.request = request;
        this.parserJson = parser;
        this.groupRepository = groupRepository;
    }

    /**
     * Метод, который обрабатывает номер группы; присваивает и возвращает внутренний номер группы
     *
     * @param chatId внутренний номер часа
     * @param group  номер группы
     * @return внутренний номер группы
     */
    @Override
    public String convertAndUpdateNumberOfGroup(String group, String chatId) {
        String response = request.getInnerNumber(group);
        parserJson.parseJson(response);

        if (responseValid(response, group)) {
            if (parserJson.notEmpty()) {
                groupRepository.setGroupNumber(chatId, String.valueOf(parserJson.getData()));
                return groupRepository.getGroupNumber(chatId);
            }
        }
        groupRepository.setGroupNumber(chatId, null);
        return null;
    }

    private boolean responseValid(String response, String group) {
        return response != null && response.length() > 6 && group.length() > 6;
    }

    /**
     * Метод, сбрасывающий или меняющий внутренний номер группы
     *
     * @param chatId внутренний номер чата
     * @return Сообщение об ошибке/об успешном исходе
     */

    @Override
    public String checkGroupChange(String chatId) {
        if (groupRepository.getGroupNumber(chatId) == null) {
            return Report.REQUEST_ERROR;
        } else {
            return Report.GROUP_CHANGE;
        }
    }
}
