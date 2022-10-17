package org.bot.Http;


public interface IHttpRequest {
    String getInnerNumber(String group);

    String getSchedule(String innerNumber);
}
