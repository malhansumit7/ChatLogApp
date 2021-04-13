package com.sumit.chatLogs.chat;

import com.sumit.chatLogs.controller.ChatLogsController;
import com.sumit.chatLogs.dto.MessageLogReqDTO;
import com.sumit.chatLogs.dto.MessageResponseDTO;
import com.sumit.chatLogs.dto.ReplyDTO;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@SpringBootTest
class ChatApplicationTests {

    @Autowired
    ChatLogsController chatController;

    @Test
    void contextLoads() {
        Assertions.assertThat(chatController).isNotNull();
    }

    @Test
    void addMessageLogTest() {
        MessageLogReqDTO messageLogReqDTO = new MessageLogReqDTO();
        messageLogReqDTO.setSent(true);
        messageLogReqDTO.setMessage("Test Message from JUNIT");
        messageLogReqDTO.setMessageTime(new Date(System.currentTimeMillis()).getTime());
        ReplyDTO reply = chatController.addMessageLogJSONEncoded(messageLogReqDTO, " ", "JUNIT");
        Assertions.assertThat(reply.getData()).isNotNull();
    }
    @Test
    void getMessageLogsTest(){
        ThreadLocalRandom threadLocalRandom=ThreadLocalRandom.current();
        Optional<Integer> start=Optional.of(threadLocalRandom.nextInt(100));
        Optional<Integer> limit=Optional.of(threadLocalRandom.nextInt(10));
        ReplyDTO reply=chatController.getMessageLogs(limit,start,"JUNIT"," ");
        Assertions.assertThat(reply.getData()).isNotNull();
        List<MessageResponseDTO> messages= (List<MessageResponseDTO>) reply.getData();
        Assertions.assertThat(messages.size()).isLessThanOrEqualTo(limit.get());
        //Integer minMessageId=messages.stream().map(MessageResponseDto::getMessageId).collect(Collectors.minBy((id1,id2)->Integer.compare(id1,id2))).orElse(0);
        Date prevDate=null;
        for(MessageResponseDTO messageResponseDto:messages){
            Assertions.assertThat(messageResponseDto.getUserId()).isEqualTo("JUNIT");
            Assertions.assertThat(messageResponseDto.getMessageId()).isGreaterThanOrEqualTo(start.get());
            if(prevDate!=null)
                Assertions.assertThat(messageResponseDto.getTimestamp()).isBeforeOrEqualTo(prevDate);
            prevDate=messageResponseDto.getTimestamp();
        }
    }
    @Test
    void deleteLogs(){
        String user="JUNIT";
        ThreadLocalRandom threadLocalRandom=ThreadLocalRandom.current();
        Optional<Integer> messageId=Optional.of(threadLocalRandom.nextInt(100));
        ResponseEntity responseEntity=chatController.deleteLog("",user,messageId);
        Assertions.assertThat(responseEntity.getBody()).isNotNull();
        ReplyDTO reply= (ReplyDTO) responseEntity.getBody();
        Assertions.assertThat(reply.getData()).isNotNull();
        int deletedCount= (int) reply.getData();
        if(deletedCount==0)
            Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        else
            Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    



}
