package com.example.sqs.publisher;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.example.sqs.domain.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

@Component
@Slf4j
@EnableScheduling
public class PublisherFIFO implements Publisher{

    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;
    @Autowired
    private AmazonSQSAsync amazonSQSAsync;

    @Value("${cloud.aws.uri.fifo}")
    private String fifoQueueUrl;

    private static final String GROUP_ID ="grp-id-example";
    private static final int MAX_MESSAGE_QUANTITY = 10;
    private static final ObjectMapper objectMapper = new ObjectMapper();;

    @Override
    @Scheduled(fixedRate = 2000)
    public void publisherInSqs() {
        //log.info("Sending Message to SQS ");

        var message = "sqs message";
        Person person = new Person(message, LocalDate.now(), UUID.randomUUID().toString());

        //FORMA 1
        //var headers = buildHeaderToMessageSqs(GROUP_ID, UUID.randomUUID().toString());
        //queueMessagingTemplate.convertAndSend(fifoQueueUrl, person, headers);

        //FORMA 2
        //queueMessagingTemplate.send(fifoQueueUrl, MessageBuilder.withPayload(person).build());

        //FORMA 3
        //publisherWithAmazonSQSObject(person);

        //FORMA 4
        //publisherBatchInSqs();
    }

    private Map<String, Object> buildHeaderToMessageSqs(String messageGroupId, String messageDeduplicationId){
        Map<String, Object> headers = new HashMap<>();
        headers.put("message-group-id", messageGroupId);
        //headers.put("message-deduplication-id", messageDeduplicationId); //duplicação baseada em conteudo esta ativo na fila
        return headers;
    }

    private void publisherWithAmazonSQSObject(Person person){

        var personAsString = writeValueAsString(person);

        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(fifoQueueUrl)
                .withMessageBody(personAsString)
                .withMessageGroupId(GROUP_ID);
                //.withMessageDeduplicationId(); //duplicação baseada em conteudo esta ativo na fila
        var returnRequest = amazonSQSAsync.sendMessage(request);
        log.info("result of request {} ", returnRequest);
    }

    private void publisherBatchInSqs(){

        var messageEntries = prepareBatchMessages();

        SendMessageBatchRequest sendMessageBatchRequest = new SendMessageBatchRequest(fifoQueueUrl, messageEntries);
        var returnRequest = amazonSQSAsync.sendMessageBatch(sendMessageBatchRequest);
        log.info("result of request {} ", returnRequest);
    }

    private List<SendMessageBatchRequestEntry> prepareBatchMessages() {

        List<SendMessageBatchRequestEntry> messageEntries = new ArrayList<>();

        IntStream.range(0, MAX_MESSAGE_QUANTITY).forEach(i -> {
            messageEntries.add(new SendMessageBatchRequestEntry()
                    .withId("id-"+UUID.randomUUID().toString())
                    .withMessageBody("batch-"+i)
                    .withMessageGroupId(GROUP_ID));
        });

        return messageEntries;
    }

    public String writeValueAsString(Object value){
        String valueAsString = null;

        try {
            valueAsString = objectMapper.writeValueAsString(value) ;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return valueAsString;
    }

    // lembrar de descrever variavel de ambiente setada no readme

}
