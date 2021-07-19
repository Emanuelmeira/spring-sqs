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

//    @SqsListener(value = "standardQueue", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
//    public void testSendMessageToDLQ(final Person person) { // @Header("SenderId") String senderId
//        //deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS
//        //essa politica deleta a mensagem da fila se a mesma for processada e NENHUM ERRO NAO TRATADO for lançado
//
//        log.info("message received {} ",person);
//
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException | RuntimeException e) {
//            e.printStackTrace();
//        }
//
//        throw new RuntimeException();
//        //OBS: sistema de DLQ já esta criado no AWS
//    }

}
