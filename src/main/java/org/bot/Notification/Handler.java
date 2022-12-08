package org.bot.Notification;

import org.bot.Entity.Ntf;
import org.bot.Entity.SendMessage;
import org.bot.Services.NtfServices;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class Handler {
    private final NtfServices ntfServices;
    public static TreeMap<Date, Long> treeMap;


    public Handler() {
        this.ntfServices = new NtfServices();
        treeMap = new TreeMap<>();
    }

    public void createTreeMap() {
        List<SendMessage> notSendMessage = ntfServices.getAllNotSendMessage();
        for (int i = 0; i < notSendMessage.size(); i++) {
            Ntf ntf = ntfServices.findById(String.valueOf(notSendMessage.get(i)));
            treeMap.put(ntf.getDate(), ntf.getId());
        }
    }
}
