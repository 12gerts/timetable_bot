package org.bot.Parser;

/**
 * Класс, являющийся шаблоном для конвертации запроса в JSON
 */
public class TemplateJson {
    /**
     * Поле, хранящее массив с JSON
     */
    private Suggestion[] suggestions;

    public Suggestion[] getSuggestions() {
        return suggestions;
    }

}
