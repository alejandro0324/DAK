package application.dak.DAK.backend.expeditions.components;

import application.dak.DAK.backend.common.models.Package;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static application.dak.Configuration.ActiveMQConfig.EXPEDITIONS_QUEUE;

@Component
@Slf4j
@AllArgsConstructor
public class ExpeditionSender {
    private final JmsTemplate jmsTemplate;

    public void notifyPackagesUpdate(Package pack) {
        log.info("message: {}", pack);
        Gson gson = new Gson();
        String json = gson.toJson(pack);
        jmsTemplate.convertAndSend(EXPEDITIONS_QUEUE, json);
    }
}
