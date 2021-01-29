package server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import server.dao.FeedDAO;

public class JobHandler implements RequestHandler<SQSEvent, Void> {
    @Override
    public Void handleRequest(SQSEvent sqsEvent, Context context) {
        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        FeedDAO feedDAO = new FeedDAO();

        for (SQSEvent.SQSMessage msg : sqsEvent.getRecords()) {
            Map<String, SQSEvent.MessageAttribute> attributes = msg.getMessageAttributes();
            String statusAlias = attributes.get("statusAlias").getStringValue();
            String message = attributes.get("message").getStringValue();
            String timestamp = attributes.get("timestamp").getStringValue();
            String aliases = msg.getBody().replace("[", "").replace("]", "");
            List<String> aliasBatch = Arrays.asList(aliases.split(","));
            feedDAO.batchPutFeed(aliasBatch, message, timestamp, statusAlias);
        }

        return null;
    }
}
