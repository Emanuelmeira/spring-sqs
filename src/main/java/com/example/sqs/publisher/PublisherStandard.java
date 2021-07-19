package com.example.sqs.publisher;

import com.example.sqs.domain.Person;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@Slf4j
@EnableScheduling
public class PublisherStandard implements Publisher{

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Value("${cloud.aws.uri.standard}")
    private String standardQueueUrl;

    @Override
    @Scheduled(fixedRate = 5000)
    public void publisherInSqs() {

        //log.info("Sending Message to SQS ");

        var message = "sqs message";
        Person person = new Person(message, LocalDate.now(), UUID.randomUUID().toString());

        //queueMessagingTemplate.convertAndSend(standardQueueUrl, person);
    }
}
