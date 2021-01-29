package server.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.domain.Story;
import model.net.TweeterRemoteException;
import model.service.NewTweetService;
import model.service.request.NewTweetRequest;
import model.service.response.NewTweetResponse;
import server.dao.AuthTokenDAO;
import server.dao.StoryDAO;

public class NewTweetServiceImpl implements NewTweetService {
    private String postQueueURL = "https://sqs.us-west-2.amazonaws.com/339639012187/PostsQueue";

    @Override
    public NewTweetResponse postTweet(NewTweetRequest request) {
        if (request.getStatus() != null) {
            if (!getAuthDAO().validAuth(request.getStatus().getUser().getAlias())) {
                return new NewTweetResponse("Your session has expired, please log back in");
            }
        }
        updatePostQueue(request);
        return getStoryDAO().postTweet(request);
    }

    public boolean updatePostQueue(NewTweetRequest request) {

        Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
        messageAttributes.put("alias",
                new MessageAttributeValue()
                        .withDataType("String")
                        .withStringValue(request.getStatus().getUser().getAlias()));
        messageAttributes.put("message",
                new MessageAttributeValue()
                    .withDataType("String")
                    .withStringValue(request.getStatus().getMessage()));
        messageAttributes.put("timestamp",
                new MessageAttributeValue()
                    .withDataType("String")
                    .withStringValue(request.getStatus().getTimestampString()));

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(postQueueURL)
                .withMessageBody("status")
                .withMessageAttributes(messageAttributes);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        sqs.sendMessage(sendMessageRequest);
        return true;
    }

    public StoryDAO getStoryDAO(){ return new StoryDAO(); }
    public AuthTokenDAO getAuthDAO() { return new AuthTokenDAO(); }
}
