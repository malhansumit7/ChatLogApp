package com.sumit.chatLogs.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumit.chatLogs.util.Constant;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class ReplyDTO {
        Object data;
        String token;
        String message;
        short messageType;
        @JsonIgnore
        Map<String,Object> session;
        @JsonIgnore
        @Autowired
        ObjectMapper mapper;

    public ReplyDTO(Map<String, Object> session) {
        this.session = session;
    }

    public ReplyDTO() {
        session=new HashMap<>();
    }

        public void setMessage(String message, Constant.MessageType messageType){
        setMessage(message);
        setMessageType(messageType.getKey());

    }
        public void addToSession(String key,Object value){
        session.put(key,value);
    }
        public Object getFromSession(String key){

        return session.get(key);

    }

        public <T> T getFromSession(String key,Class<T> valueClass){
        try {
            return mapper.readValue(new JSONObject((Map) session.get(key)).toString(),valueClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

        public Object getData() {
        return data;
    }

        public void setData(Object data) {
        this.data = data;
    }

        public String getToken() {
        return token;
    }

        public void setToken(String token) {
        this.token = token;
    }

        public String getMessage() {
        return message;
    }

        public void setMessage(String message) {
        this.message = message;
    }

        public short getMessageType() {
        return messageType;
    }

        public void setMessageType(short messageType) {
        this.messageType = messageType;
    }

        public Map<String, Object> getSession() {
        return session;
    }

        public void setSession(Map<String, Object> session) {
        this.session = session;
    }

}
