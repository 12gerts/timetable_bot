package org.bot.Parser;

public interface IParserJson {
    void parseJson(String response);

    boolean notEmpty();

    String getData();
}
