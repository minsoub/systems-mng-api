package com.bithumbsystems.management.api.core.util;

import org.springframework.stereotype.Service;

@Service
public interface MailService {

  void send(String email);
}
