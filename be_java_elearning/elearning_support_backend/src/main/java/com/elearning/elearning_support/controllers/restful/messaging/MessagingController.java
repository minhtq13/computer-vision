package com.elearning.elearning_support.controllers.restful.messaging;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.elearning.elearning_support.constants.KafkaConstants;
import com.elearning.elearning_support.dtos.kafka.KafkaMessageDTO;
import com.elearning.elearning_support.services.kafka.KafkaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/messaging")
@RequiredArgsConstructor
public class MessagingController {

    private final KafkaService kafkaService;


    @PostMapping("/kafka/send")
    @Operation(description = "Gửi một bản tin Kafka")
    public KafkaMessageDTO sendKafkaMessage(@RequestBody Object data) {
        return kafkaService.sendMessage(KafkaConstants.ELS_DEFAULT_TOPIC_NAME, "example", data, null);
    }
}
