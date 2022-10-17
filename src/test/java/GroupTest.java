import org.bot.Group;
import org.bot.Http.IHttpRequest;
import org.bot.Parser.IParserJson;
import org.bot.Report;
import org.bot.Telegram.Telegram;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GroupTest {

    @Test
    public void getConvertNumberOfGroupForExist() {
        Telegram.map.put("1", null);
        Group group = new Group(new MockHttpRequest(), new MockParserJson());
        String actual = group.convertAndUpdateNumberOfGroup("мен-210204", "1");
        String expected = "54627";
        assertEquals(expected, actual);
    }

    @Test
    public void getConvertNumberOfGroupForNonExist() {
        Telegram.map.put("1", null);
        Group group = new Group(new MockHttpRequest(), new MockParserJson());
        String actual = group.convertAndUpdateNumberOfGroup("мен-00000", "1");
        assertNull(actual);
    }

    @Test
    public void checkGroupChangeIfNull() {
        Telegram.map.put("1", null);
        Group group = new Group(new MockHttpRequest(), new MockParserJson());
        String actual = group.checkGroupChange("1");
        String expected = Report.REQUEST_ERROR;
        assertEquals(expected, actual);
    }

    @Test
    public void checkGroupChangeIfNotNull() {
        Telegram.map.put("1", "123");
        Group group = new Group(new MockHttpRequest(), new MockParserJson());
        String actual = group.checkGroupChange("1");
        String expected = Report.GROUP_CHANGE;
        assertEquals(expected, actual);
    }

    public static class MockHttpRequest implements IHttpRequest {

        @Override
        public String getInnerNumber(String group) {
            String json = "{\"suggestions\": [{\"value\": \"\u041c\u0415\u041d-210204\", \"data\": 54627}]}";
            if (Objects.equals(group, "мен-210204")) {
                return json;
            }
            return "{\"suggestions\": []};";
        }

        @Override
        public String getSchedule(String innerNumber) {
            return null;
        }
    }

    public static class MockParserJson implements IParserJson {
        private String response;
        @Override
        public void parseJson(String response) {
            if (Objects.equals(response, "{\"suggestions\": [{\"value\": \"\u041c\u0415\u041d-210204\", \"data\": 54627}]}")) {
                this.response = "1";
            } else {
                this.response = "0";
            }
        }

        @Override
        public boolean notEmpty() {
            return Objects.equals(response, "1");
        }

        @Override
        public String getData() {
            if (Objects.equals(response, "1")) {
                return "54627";
            } return null;
        }
    }

}
