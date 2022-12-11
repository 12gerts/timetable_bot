package org.bot.Parser;

import com.google.gson.Gson;


public class ParserJson implements IParserJson {
    private TemplateJson json;

    @Override
    public void parseJson(String response) {
        json = new Gson().fromJson(response, TemplateJson.class);
    }

    @Override
    public boolean notEmpty() {
        return json.getSuggestions().length != 0;
    }

    private Suggestion getSuggestions() {
        return json.getSuggestions()[0];
    }

    @Override
    public String getData() {
        return getSuggestions().getData();
    }

    public String getValue() {
        return getSuggestions().getValue();
    }

}
