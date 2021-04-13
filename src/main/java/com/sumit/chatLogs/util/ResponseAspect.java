package com.sumit.chatLogs.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumit.chatLogs.dto.ReplyDTO;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Aspect
public class ResponseAspect {
    private final Helper helper;
    private final ObjectMapper mapper;

    public ResponseAspect(Helper helper, ObjectMapper mapper) {
        this.helper = helper;
        this.mapper = mapper;
    }

    @AfterReturning(pointcut = "@annotation(org.springframework.web.bind.annotation.GetMapping)||" +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)||" +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping)", returning = "response")
    public void afterPostReturningForLogin(Object response) {
        ReplyDTO reply=null;
        if(response instanceof ReplyDTO) {
            reply = (ReplyDTO) response;
        }
        else if(response instanceof ResponseEntity){
           reply=(ReplyDTO)((ResponseEntity)response).getBody();
        }
        if(reply!=null)
            reply.setToken(generateToken(reply.getSession()));
    }

    private String generateToken(Map<String, Object> session) {
        try {
            return helper.generateToken(mapper.writeValueAsString(session));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
