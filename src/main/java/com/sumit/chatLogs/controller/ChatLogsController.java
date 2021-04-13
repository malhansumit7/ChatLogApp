package com.sumit.chatLogs.controller;

import com.sumit.chatLogs.dto.MessageLogReqDTO;
import com.sumit.chatLogs.dto.MessageResponseDTO;
import com.sumit.chatLogs.dto.ReplyDTO;
import com.sumit.chatLogs.service.ChatLogsService;
import com.sumit.chatLogs.util.Constant;
import com.sumit.chatLogs.util.Helper;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chatlogs/{user}")
public class ChatLogsController {
    private final Helper helper;
    private final ChatLogsService chatService;

    public ChatLogsController(Helper helper, ChatLogsService chatService) {
        this.helper = helper;
        this.chatService = chatService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,},path = "")
    public ReplyDTO addMessageLogJSONEncoded(@RequestBody @NotNull @Valid MessageLogReqDTO messageLogReqDTO, @RequestHeader(value = HttpHeaders.AUTHORIZATION,defaultValue = " ") String token,@PathVariable("user") String user){
        ReplyDTO reply=helper.getReply(token);
        reply.setData(chatService.addMessageLog(messageLogReqDTO,user));
        reply.setMessage("LOG ADDED SUCCUSSFULLY!!", Constant.MessageType.INFO);
        return reply;
    }
    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE},path = "")
    public ReplyDTO addMessageLogUrlEncoded(@NotNull @Valid MessageLogReqDTO messageLogReqDTO, @RequestHeader(value = HttpHeaders.AUTHORIZATION,defaultValue = " ") String token, @PathVariable("user") String user){
        ReplyDTO reply=helper.getReply(token);
        reply.setData(chatService.addMessageLog(messageLogReqDTO,user));
        reply.setMessage("LOG ADDED SUCCUSSFULLY!!", Constant.MessageType.INFO);
        return reply;
    }

    @GetMapping(path = "")
    public ReplyDTO getMessageLogs(@RequestParam("limit")Optional<Integer> optionalLimit,@RequestParam("start")Optional<Integer> optionalStart,@PathVariable("user") String user,@RequestHeader(value = HttpHeaders.AUTHORIZATION,defaultValue = " ") String token){
        ReplyDTO reply=helper.getReply(token);
        List<MessageResponseDTO> messageResponseDtos=chatService.getLogs(user,optionalLimit,optionalStart);
        reply.setData(messageResponseDtos);
        if(messageResponseDtos.isEmpty())
            reply.setMessage("NO RECORDS FOUND!!", Constant.MessageType.ERROR);
        return reply;
    }
    
    @DeleteMapping(path = {"","/{msgId}"})
    public ResponseEntity<ReplyDTO> deleteLog(@RequestHeader(value = HttpHeaders.AUTHORIZATION,defaultValue = " ") String token, @PathVariable("user") String user,@PathVariable("msgId") Optional<Integer> messageId){
        ReplyDTO reply=helper.getReply(token);
        
        int deletedRecords=chatService.deleteLogs(user,messageId);
        reply.setData(deletedRecords);
        HttpStatus status;
        if(deletedRecords==0){
            status=HttpStatus.NO_CONTENT;
            reply.setMessage("NO RECORD FOUND", Constant.MessageType.ERROR);
        }else{
            status=HttpStatus.OK;
            reply.setMessage("RECORDS DELETED", Constant.MessageType.INFO);
        }
        ResponseEntity<ReplyDTO> responseEntity=new ResponseEntity(reply,status);
        return responseEntity;
    }


}
