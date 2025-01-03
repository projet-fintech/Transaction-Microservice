package com.example.backend.service;

import com.banque.events.dto.AccountDto;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class KafkaService {

    @Autowired
    private ReplyingKafkaTemplate<String, UUID, AccountDto> replyingKafkaTemplate;

    @Autowired
    KafkaTemplate<String, AccountDto> kafkaTemplate;

    @Value("${spring.kafka.topic.account-details-request-topic}")
    private String accountRequestTopic;

    @Value("${spring.kafka.topic.account-update-topic}")
    private String accountUpdateTopic;

    public AccountDto getAccountById(UUID accountId) throws ExecutionException, InterruptedException, TimeoutException {
        // create the request with the necessary account Id
        ProducerRecord<String, UUID> producerRecord = new ProducerRecord<>(accountRequestTopic, accountId);
        RequestReplyFuture<String, UUID, AccountDto> sendAndRecieve = replyingKafkaTemplate.sendAndReceive(producerRecord, Duration.ofSeconds(30));

        try {
            ConsumerRecord<String, AccountDto> response = sendAndRecieve.get(30, TimeUnit.SECONDS);
            return response.value();
        }catch (TimeoutException e) {
            throw new RuntimeException("Timeout waiting for account service respnse",e);
        }
    }

    public void updateAccountBalance(AccountDto compteSource, AccountDto cmpteCible, double amount){
        // Décrémenter le compte source et incrémenter le compte cible
        compteSource.setBalance(compteSource.getBalance()-amount);
        cmpteCible.setBalance(cmpteCible.getBalance()+amount);

        // Envoyer les comptes mis à jour au microservice des comptes
        kafkaTemplate.send(accountUpdateTopic,compteSource);
        kafkaTemplate.send(accountUpdateTopic,cmpteCible);
    }

    public void updateAccountBalanceVersement(AccountDto account, double amount) {
        // Incrémenter le solde du compte
        account.setBalance(account.getBalance()+amount);
        // Envoyer le compte mis à jour au microservice des comptes
        kafkaTemplate.send(accountUpdateTopic,account);
    }

    public void updateAccountBalanceRetrait(AccountDto account, double amount){
        //décrémenter le solde du compte
        account.setBalance(account.getBalance() - amount);
        // Envoyer le compte mis à jour au microservice des comptes
        kafkaTemplate.send(accountUpdateTopic,account);
    }
}
