package main.java.com.mercari.merpay.pubsub.model;

import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Topic {
    private final String topicName;
    private final String publisherId; // considering only 1 owner
    private final List<Message> messages;
    private final List<TopicSubscriber> subscribers;

    public Topic(@NonNull final String topicName, @NonNull final String publisherId) {
        this.topicName = topicName;
        this.publisherId = publisherId;
        this.messages = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }

    public synchronized void addMessage(@NonNull String publisherId, @NonNull final Message message) throws Exception {
        if (!this.publisherId.equals(publisherId)) {
            throw new Exception("unauthorized to publish to" + topicName + "which is owned by another publisher");
        }
        messages.add(message);
    }

    public void addSubscriber(@NonNull final TopicSubscriber subscriber) {
        subscribers.add(subscriber);
    }

}
