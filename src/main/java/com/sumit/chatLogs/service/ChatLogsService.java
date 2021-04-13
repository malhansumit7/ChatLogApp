package com.sumit.chatLogs.service;

import com.sumit.chatLogs.dto.MessageLogReqDTO;
import com.sumit.chatLogs.dto.MessageResponseDTO;
import com.sumit.chatLogs.entity.MessageTbl;
import com.sumit.chatLogs.repo.MessageTblRepo;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatLogsService {
    private final MessageTblRepo messageTblRepo;
    private final EntityManager entityManager;

    public ChatLogsService(MessageTblRepo messageTblRepo, EntityManager entityManager) {
        this.messageTblRepo = messageTblRepo;
        this.entityManager = entityManager;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public int addMessageLog(MessageLogReqDTO messageLogReqDTO,String user){
        MessageTbl messageTbl=new MessageTbl();
        messageTbl.setMessage(messageLogReqDTO.getMessage());
        messageTbl.setUserId(user);
        messageTbl.setSent(messageLogReqDTO.isSent());
        messageTbl.setTimestamp(messageLogReqDTO.getMessageTimeAsDate());
        messageTbl=messageTblRepo.save(messageTbl);
        return messageTbl.getMessageId();
    }
    
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public int deleteLogs(String user, Optional<Integer> optionalMesageId){
        return messageTblRepo.deleteLog(user,optionalMesageId,entityManager);
    }
   
    public List<MessageResponseDTO> getLogs(String user,Optional<Integer> optionalLimit,Optional<Integer>optionalStart){
        
    	List<MessageTbl> messageTbls= messageTblRepo.findAllByUserIdAndMessageIdAfterOrderByTimestampDesc(user,optionalStart.orElse(1)-1,PageRequest.of(0,optionalLimit.orElse(10)));
        
        List<MessageResponseDTO> responseDtos=messageTbls.stream().map(messageTbl -> {
            MessageResponseDTO responseDto=new MessageResponseDTO();
            BeanUtils.copyProperties(messageTbl,responseDto);
            return responseDto;
        }).collect(Collectors.toList());
        return responseDtos;
    }

}
