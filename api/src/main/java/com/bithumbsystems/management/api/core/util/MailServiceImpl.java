package com.bithumbsystems.management.api.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

  @Override
  public void send(String email) {
    log.info("send email target: {}", email);
  }
}
