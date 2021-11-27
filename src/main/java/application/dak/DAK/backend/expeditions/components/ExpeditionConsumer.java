package application.dak.DAK.backend.expeditions.components;

import application.dak.DAK.backend.common.models.Package;
import application.dak.DAK.backend.expeditions.services.ExpeditionService;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static application.dak.Configuration.ActiveMQConfig.EXPEDITIONS_QUEUE;

@Component
@Slf4j
@AllArgsConstructor
public class ExpeditionConsumer {
    private final ExpeditionService expeditionService;

    @JmsListener(destination = EXPEDITIONS_QUEUE)
    public void receiveMessage(@Payload String payload) {
        log.info("Package message: {}", payload);
        Gson gson = new Gson();
        Package pack = gson.fromJson(payload, Package.class);
        expeditionService.updatePackageSync(pack);
    }
}
