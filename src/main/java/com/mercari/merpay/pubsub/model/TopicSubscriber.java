package main.java.com.mercari.merpay.pubsub.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@AllArgsConstructor
public class TopicSubscriber {
    private final AtomicInteger offset;
    private final String subscriberId;

    public TopicSubscriber(@NonNull @NotBlank String subscriberId) {
        this.subscriberId = subscriberId;
        this.offset = new AtomicInteger(0);
    }

}
