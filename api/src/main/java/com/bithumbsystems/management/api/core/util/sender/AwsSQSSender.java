package com.bithumbsystems.management.api.core.util.sender;

import com.amazonaws.services.sqs.model.SendMessageResult;
import com.bithumbsystems.management.api.core.model.request.AccessAllowIpRequest;
import com.bithumbsystems.persistence.mongodb.role.model.entity.AuthorizationResource;
import java.util.List;

public interface AwsSQSSender<T> {

  SendMessageResult sendMessage(AccessAllowIpRequest request, String groupId);

  SendMessageResult sendMessage(List<AuthorizationResource> authorizationResources,
      String roleManagementId);
}
