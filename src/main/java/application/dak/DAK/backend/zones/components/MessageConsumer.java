package application.dak.DAK.backend.zones.components;

import application.dak.DAK.backend.common.dto.Coordinate;
import application.dak.DAK.backend.common.dto.Zone;
import application.dak.DAK.backend.common.messages.CoordinateMessage;
import application.dak.DAK.backend.common.messages.ZoneMessage;
import application.dak.DAK.backend.zones.services.ZoneService;
import com.flowingcode.vaadin.addons.googlemaps.LatLon;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static application.dak.Configuration.ActiveMQConfig.COORDINATES_QUEUE;
import static application.dak.Configuration.ActiveMQConfig.ZONES_QUEUE;

@Component
@Slf4j
@AllArgsConstructor
public class MessageConsumer {
    private final ZoneService zoneService;

    @JmsListener( destination = ZONES_QUEUE )
    public void receiveMessage(@Payload String payload) {
        log.info("Zone message: {}", payload);
        Gson gson = new Gson();
        ZoneMessage message = gson.fromJson(payload, ZoneMessage.class);
        Zone zone = new Zone(null, message.getName(), message.getUuid(), message.getCoordinates());
        zoneService.addZoneSync(zone);
    }
}
