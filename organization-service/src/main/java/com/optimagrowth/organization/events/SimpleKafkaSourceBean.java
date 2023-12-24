package com.optimagrowth.organization.events;

import com.optimagrowth.organization.events.model.OrganizationChangeModel;
import com.optimagrowth.organization.model.ActionEnum;
import com.optimagrowth.organization.utils.JsonUtil;
import com.optimagrowth.organization.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 19 Dec, 2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class SimpleKafkaSourceBean {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Environment env;

    public void publishOrganizationChange(ActionEnum action, String organizationId){
        log.info("Sending Kafka message {} for Organization Id {}", action,organizationId);
        OrganizationChangeModel change =  new OrganizationChangeModel(
                OrganizationChangeModel.class.getTypeName(),
                action.toString(),
                organizationId,
                UserContext.CORRELATION_ID);

        CompletableFuture<SendResult<String, String>> future =  kafkaTemplate.send(
                env.getProperty("spring.kafka.topic", String.class, "orgChangeTopic"),
                JsonUtil.toJson(change, false));

        future.whenComplete((result, ex) -> {
            if (ex == null) {

                log.info(" Sent action: [{}] with orgId:[{}], with offset: [{}]", action, organizationId, result.getRecordMetadata().offset());
            } else {
                log.error("Unable to sent message with action: [{}] with orgId:[{}]", action, organizationId, ex);
            }
        });
    }
}
