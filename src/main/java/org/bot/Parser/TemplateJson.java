package org.bot.Parser;

/**
 * Класс, являющийся шаблоном для конвертации запроса в JSON
 */
public class TemplateJson {
    public Suggestion[] getSuggestions() {
        return suggestions;
    }

    /**
     * Поле, хранящее массив с JSON
     */
    private Suggestion[] suggestions;

}
