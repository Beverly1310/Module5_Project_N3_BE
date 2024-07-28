package com.ra.service.imp;

import com.ra.model.entity.Message;
import com.ra.model.entity.User;
import com.ra.repository.MessageRepository;
import com.ra.repository.UserRepository;
import com.ra.security.principal.CustomUserDetail;
import com.ra.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private Long currentUserId() {
        CustomUserDetail customUserDetail = (CustomUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return customUserDetail.getId();
    }

    @Override
    public int countUnreadMessage() {
        return messageRepository.countUnreadMessage(currentUserId());
    }

    @Override
    public List<String> messages() {
        List<Message> messages = messageRepository.findAllByUserId(currentUserId());
        messages.sort((m1, m2) -> m2.getCreatedAt().compareTo(m1.getCreatedAt()));
        List<String> messageStrings = new ArrayList<>();
        for (Message message : messages) {
            messageStrings.add(message.getMessage());
        }
        return messageStrings;
    }

    @Override
    public void markAsRead() {
        messageRepository.setAsRead(currentUserId());
    }
}
