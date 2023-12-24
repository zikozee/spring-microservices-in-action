package com.optimagrowth.license.service;

import com.optimagrowth.license.model.OrganizationChangeModel;
import com.optimagrowth.license.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 20 Dec, 2023
 */

@Slf4j
@Service
public class KafkaMessageListener {

    @KafkaListener(topics = "orgChangeTopic", groupId = "licensing-group")
    public void consumes(String message){
        OrganizationChangeModel organizationChangeModel = JsonUtil.fromJSON(message, OrganizationChangeModel.class);
        log.info("Consumer consumes the message {}, POJO: {}", message, organizationChangeModel);
    }
}
