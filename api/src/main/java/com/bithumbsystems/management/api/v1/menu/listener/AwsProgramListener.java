package com.bithumbsystems.management.api.v1.menu.listener;

import com.bithumbsystems.management.api.core.config.AutoGeneratorProgramConfig;
import com.bithumbsystems.management.api.core.config.AutoGeneratorProgramConfig.Program;
import com.google.gson.Gson;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AwsProgramListener {
  private final AutoGeneratorProgramConfig autoGeneratorProgramConfig;

  @SqsListener(value = {"${cloud.aws.sqs.program.queue-name}"}, deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
  private void programMessage(@Headers Map<String, String> header, @Payload String message) {
    log.debug("header: {} message: {}", header, message);
    var program = new Gson().fromJson(message, Program.class);
    autoGeneratorProgramConfig.saveProgram(program);
  }
}
