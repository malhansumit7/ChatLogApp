package com.sumit.chatLogs.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumit.chatLogs.dto.ReplyDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class Helper {

    @Autowired
    ObjectMapper mapper;

    String JWT_SECRET = "secret";
    int expiry = 60 * 60 * 1000;


    public String generateToken(String claims) {
        Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
        String token = JWT.create().withIssuer("sumit").withIssuedAt(new Date()).withExpiresAt(getExpiryDate(expiry)).
                withClaim("claims", claims).sign(algorithm);
        return token;
    }

    private Date getExpiryDate(int minutes) {
        return new Date(Calendar.getInstance().getTimeInMillis() + minutes);
    }

    public DecodedJWT validateToken(String token) {
        try {
        	//here we can also mention the IP address of the request.
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer("sumit").build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt;
        }catch (Exception e){
            //e.printStackTrace();
        }
        return null;
    }

    public ReplyDTO getReply(String token) {
        if (token == null)
            return null;
        try {
            Map<String, Object> session =getSession(token);
            return new ReplyDTO(session);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, Object> getSession(String token) throws JsonProcessingException {
        DecodedJWT jwt = validateToken(token);
        if (jwt == null)
            return new HashMap<>();
        return mapper.readValue(jwt.getClaim("claims").asString(), HashMap.class);
    }
}
