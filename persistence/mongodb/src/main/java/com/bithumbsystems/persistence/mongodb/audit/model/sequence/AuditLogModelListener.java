package com.bithumbsystems.persistence.mongodb.audit.model.sequence;

import com.bithumbsystems.persistence.mongodb.audit.model.entity.AuditLog;
import com.bithumbsystems.persistence.mongodb.common.service.ISequenceGeneratorService;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuditLogModelListener extends AbstractMongoEventListener<AuditLog> {

  private final ISequenceGeneratorService sequenceGenerator;

  @Autowired
  public AuditLogModelListener(ISequenceGeneratorService sequenceGenerator) {
    this.sequenceGenerator = sequenceGenerator;
  }

  @Override
  public void onBeforeConvert(BeforeConvertEvent<AuditLog> event) {
    try {
      if (event.getSource().getSeq() == null) {
        event.getSource().setSeq(sequenceGenerator.generateSequence(AuditLog.SEQUENCE_NAME));
      }
    } catch (InterruptedException | ExecutionException e) {
      log.error("Error:{}", e.getMessage());
    }
  }
}
