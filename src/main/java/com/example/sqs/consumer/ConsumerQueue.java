package com.example.sqs.consumer;

import com.example.sqs.domain.Person;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ConsumerQueue {

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @SqsListener(value = "standardQueue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveMessage(final Person person) { // @Header("SenderId") String senderId
        log.info("message received {} ",person);
    }

}
