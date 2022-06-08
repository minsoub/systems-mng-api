package com.bithumbsystems.persistence.mongodb.site.model.sequence;

import com.bithumbsystems.persistence.mongodb.common.service.ISequenceGeneratorService;
import com.bithumbsystems.persistence.mongodb.site.model.entity.Site;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SiteModelListener extends AbstractMongoEventListener<Site> {

  private final ISequenceGeneratorService sequenceGenerator;

  @Autowired
  public SiteModelListener(ISequenceGeneratorService sequenceGenerator) {
    this.sequenceGenerator = sequenceGenerator;
  }

  @Override
  public void onBeforeConvert(BeforeConvertEvent<Site> event) {
    try {
      if (event.getSource().getId() == null) {
        event.getSource().setId("SITE_" + sequenceGenerator.generateSequence(Site.SEQUENCE_NAME));
      }
    } catch (InterruptedException | ExecutionException e) {
      log.error("Error:{}", e.getMessage());
    }
  }
}
