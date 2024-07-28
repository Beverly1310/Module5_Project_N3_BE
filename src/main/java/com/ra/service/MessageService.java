package com.ra.service;


import java.util.List;

public interface MessageService {
    int countUnreadMessage();
    List<String> messages();
    void markAsRead();
}
