package com.elearning.elearning_support.services.kafka;

import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import com.elearning.elearning_support.dtos.kafka.KafkaMessageDTO;

@Service
public interface KafkaService {

    KafkaMessageDTO sendMessage(String topic, String key, Object data, ListenableFutureCallback<SendResult<String, Object>> callback);

}
