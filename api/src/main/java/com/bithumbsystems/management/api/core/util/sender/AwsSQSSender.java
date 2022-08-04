package com.bithumbsystems.management.api.core.util.sender;

import com.amazonaws.services.sqs.model.SendMessageResult;

public interface AwsSQSSender<T> {

  SendMessageResult sendMessage(T request, String groupId);
}
