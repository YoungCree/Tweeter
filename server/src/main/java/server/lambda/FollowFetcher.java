package server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import server.dao.FollowingDAO;

public class FollowFetcher implements RequestHandler<SQSEvent, Void> {
    private FollowingDAO followingDAO = new FollowingDAO();
    private String jobsQueueURL = "https://sqs.us-west-2.amazonaws.com/339639012187/JobsQueue";

    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

        for (SQSEvent.SQSMessage msg : sqsEvent.getRecords()) {
            Map<String, SQSEvent.MessageAttribute> attributes = msg.getMessageAttributes();
            String statusAlias = attributes.get("alias").getStringValue();
            String message = attributes.get("message").getStringValue();
            String timestamp = attributes.get("timestamp").getStringValue();
            context.getLogger().log(statusAlias);
            context.getLogger().log(message);
            context.getLogger().log(timestamp);

            List<String> aliases = followingDAO.getFollowers(statusAlias);
            List<String> aliasBatch = new ArrayList<>();
            Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
            messageAttributes.put("message",
                    new MessageAttributeValue()
                            .withDataType("String")
                            .withStringValue(message));
            messageAttributes.put("timestamp",
                    new MessageAttributeValue()
                            .withDataType("String")
                            .withStringValue(timestamp));
            messageAttributes.put("statusAlias",
                    new MessageAttributeValue()
                            .withDataType("String")
                            .withStringValue(statusAlias));
            for (String alias : aliases) {
                aliasBatch.add(alias);

                if (aliasBatch.size() == 25) {
                    SendMessageRequest sendMessageRequest = new SendMessageRequest()
                            .withQueueUrl(jobsQueueURL)
                            .withMessageBody(aliasBatch.toString())
                            .withMessageAttributes(messageAttributes);
                    sqs.sendMessage(sendMessageRequest);
                    aliasBatch.clear();
                }
            }

            if (aliasBatch.size() > 0) {
                SendMessageRequest sendMessageRequest = new SendMessageRequest()
                        .withMessageBody(aliasBatch.toString())
                        .withQueueUrl(jobsQueueURL)
                        .withMessageAttributes(messageAttributes);
                sqs.sendMessage(sendMessageRequest);
                aliasBatch.clear();
            }
        }
        return null;
    }
}
