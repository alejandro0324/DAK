package application.dak.DAK.backend.zones.components;

import application.dak.DAK.backend.common.messages.CoordinateMessage;
import application.dak.DAK.backend.common.messages.ZoneMessage;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static application.dak.Configuration.ActiveMQConfig.COORDINATES_QUEUE;
import static application.dak.Configuration.ActiveMQConfig.ZONES_QUEUE;

@Component
@Slf4j
@AllArgsConstructor

public class ZoneMessageSender {
    private final JmsTemplate jmsTemplate;

    public void sendNewZoneNotification(ZoneMessage message) {
        log.info("message: {}", message);
        Gson gson = new Gson();
        String json = gson.toJson(message);
        jmsTemplate.convertAndSend(ZONES_QUEUE, json);
    }

    public void sendNewCoordinateNotification(CoordinateMessage message) {
        log.info("message: {}", message);
        jmsTemplate.convertAndSend(COORDINATES_QUEUE, message);
    }
}
