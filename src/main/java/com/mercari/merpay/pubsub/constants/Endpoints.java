package main.java.com.mercari.merpay.pubsub.constants;

public class Endpoints {
    private Endpoints() {
    }

    public static final String PUBLISHER_REGISTER_TOPIC = "/topic/register";
    public static final String PUBLISHER_PUBLISH_MESSAGE = "/message/publish";
    public static final String SUBSCRIBER_SUBSCRIBE_TOPIC = "/topic/subscribe";
    public static final String SUBSCRIBER_RETRIEVE_MESSAGE = "/message/get";
    public static final String SUBSCRIBER_ACK_MESSAGE = "/message/ack";

}
