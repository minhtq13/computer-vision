package com.elearning.elearning_support.dtos.kafka;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaMessageDTO implements Serializable {

    String key;

    String topic;

    Object data;

    String messageId;

    Integer statusCode;

}
