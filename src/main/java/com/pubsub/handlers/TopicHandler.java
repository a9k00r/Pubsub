package com.pubsub.handlers;

import com.pubsub.model.Message;
import com.pubsub.model.Topic;
import com.pubsub.model.TopicSubscriber;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class TopicHandler {
    private final Topic topic;
    private final Map<String, TopicSubscriber> subscriberWorkers; // subscriberId to subscriberHandler map

    public TopicHandler(@NonNull final Topic topic) {
        this.topic = topic;
        subscriberWorkers = new ConcurrentHashMap<>();
    }

    public void addTopicSubscriber(TopicSubscriber topicSubscriber) {
        subscriberWorkers.putIfAbsent(topicSubscriber.getSubscriberId(), topicSubscriber);
    }

    public Message consumeMessage(@NotBlank String subscriberId) throws Exception {
        validateSubscriber(subscriberId);
        synchronized (TopicHandler.class) {
            AtomicInteger currOffset = subscriberWorkers.get(subscriberId).getOffset();
            if (currOffset.get() < topic.getMessages().size()) {
                return topic.getMessages().get(currOffset.get());
            }
        }
        throw new Exception("No new message");
    }

    public void ackMessage(String subscriberId) throws Exception {
        validateSubscriber(subscriberId);

        synchronized (subscriberWorkers) {
            AtomicInteger currOffset = subscriberWorkers.get(subscriberId).getOffset();
            if (currOffset.get() < topic.getMessages().size()) {
                currOffset.compareAndSet(currOffset.get(), currOffset.get() + 1);
            }

            throw new Exception("No more message left to ack please wait..");

        }
    }

    private void validateSubscriber(String subscriberId) throws Exception {
        if (!subscriberWorkers.containsKey(subscriberId)) {
            throw new Exception("invalid subscriber id, you need to subscribe to the topic first");
        }
    }

}
