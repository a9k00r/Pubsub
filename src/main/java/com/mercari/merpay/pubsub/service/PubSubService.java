package main.java.com.mercari.merpay.pubsub.service;

import com.mercari.merpay.pubsub.commons.BaseAPIResponse;
import com.mercari.merpay.pubsub.dtos.publishers.PublisherMessageDto;
import com.mercari.merpay.pubsub.dtos.publishers.PublisherRegisterDto;
import com.mercari.merpay.pubsub.dtos.subsribers.SubscribeDto;
import com.mercari.merpay.pubsub.model.Message;
import com.mercari.merpay.pubsub.model.Topic;
import com.mercari.merpay.pubsub.model.TopicSubscriber;
import com.mercari.merpay.pubsub.handlers.TopicHandler;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PubSubService {
    private final Map<String, TopicHandler> topicProcessors = new ConcurrentHashMap<>(); // topicNameToTopicHandlerMap

    public void registerAndCreateTopic(@NonNull @Valid PublisherRegisterDto publisherRegisterDto) {
        final Topic topic = new Topic(publisherRegisterDto.getTopicName(), publisherRegisterDto.getPublisherId());
        TopicHandler topicHandler = new TopicHandler(topic);
        topicProcessors.put(topic.getTopicName(), topicHandler);
        System.out.println("Created topic: " + topic.getTopicName());
    }

    public BaseAPIResponse publishMessageToTopic(@NonNull @Valid PublisherMessageDto publisherMessageDto) throws Exception {
        validateAndGetTopic(publisherMessageDto);
        Topic topic = validateAndGetTopic(publisherMessageDto);

        topic.addMessage(publisherMessageDto.getPublisherId(), new Message(publisherMessageDto.getMessage()));
        System.out.println(publisherMessageDto.getMessage() + " published to topic: " + topic.getTopicName());
        return new BaseAPIResponse(String.valueOf(HttpStatus.OK), "successfully published");
    }

    public BaseAPIResponse subscribe(@NonNull @Valid final SubscribeDto subscribeDto) throws Exception {
        Topic topic = validateAndGetTopic(subscribeDto.getTopicName());
        TopicSubscriber topicSubscriber = new TopicSubscriber(subscribeDto.getSubscriberId());
        topic.addSubscriber(topicSubscriber);
        topicProcessors.get(topic.getTopicName()).addTopicSubscriber(topicSubscriber);
        System.out.println(subscribeDto.getSubscriberId() + " subscribed to topic: " + topic.getTopicName());
        return new BaseAPIResponse(String.valueOf(HttpStatus.OK), "successfully subscribed");
    }

    public BaseAPIResponse consumeMessage(@NonNull @Valid final SubscribeDto subscribeDto) throws Exception {
        Topic topic = validateAndGetTopic(subscribeDto.getTopicName());

        TopicHandler topicHandler = topicProcessors.get(topic.getTopicName());

        final Message message = topicHandler.consumeMessage(subscribeDto.getSubscriberId());
        System.out.println("successfully consumed message : " + message.getPayload());
        return new BaseAPIResponse(String.valueOf(HttpStatus.OK), message.getPayload());
    }

    public BaseAPIResponse ackMessage(SubscribeDto subscribeDto) throws Exception {
        Topic topic = validateAndGetTopic(subscribeDto.getTopicName());
        TopicHandler topicHandler = topicProcessors.get(topic.getTopicName());
        topicHandler.ackMessage(subscribeDto.getSubscriberId());
        return new BaseAPIResponse(String.valueOf(HttpStatus.OK), "successfully acknowledged");
    }

    private Topic validateAndGetTopic(PublisherMessageDto publisherMessageDto) throws Exception {
        Topic topic = validateAndGetTopic(publisherMessageDto.getTopicName());

        if (!topicProcessors.get(publisherMessageDto.getTopicName())
                .getTopic().getPublisherId().equals(publisherMessageDto.getPublisherId())) {
            throw new Exception("unauthorised publisher");
        }
        return topic;
    }

    private Topic validateAndGetTopic(String topicName) throws Exception {
        if (!topicProcessors.containsKey(topicName)) {
            throw new Exception("Invalid topic name");
        }
        return topicProcessors.get(topicName).getTopic();

    }

}
