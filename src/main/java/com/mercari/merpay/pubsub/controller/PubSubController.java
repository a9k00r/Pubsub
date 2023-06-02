package main.java.com.mercari.merpay.pubsub.controller;

import com.mercari.merpay.pubsub.commons.BaseAPIResponse;
import com.mercari.merpay.pubsub.constants.Endpoints;
import com.mercari.merpay.pubsub.dtos.publishers.PublisherMessageDto;
import com.mercari.merpay.pubsub.dtos.publishers.PublisherRegisterDto;
import com.mercari.merpay.pubsub.dtos.subsribers.SubscribeDto;
import com.mercari.merpay.pubsub.service.PubSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class PubSubController {
    @Autowired
    private PubSubService pubSubService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/health", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseAPIResponse> healthCheck(HttpServletRequest servletRequest) throws Exception {
        BaseAPIResponse response = new BaseAPIResponse(String.valueOf(HttpStatus.OK), "running...");
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = Endpoints.PUBLISHER_REGISTER_TOPIC, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseAPIResponse> registerPublisher(HttpServletRequest servletRequest, @Valid @RequestBody PublisherRegisterDto publisherRegisterDto) {
        pubSubService.registerAndCreateTopic(publisherRegisterDto);
        BaseAPIResponse response = new BaseAPIResponse(String.valueOf(HttpStatus.CREATED), "topic created");
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = Endpoints.PUBLISHER_PUBLISH_MESSAGE, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseAPIResponse> publishMessage(HttpServletRequest servletRequest, @Valid @RequestBody PublisherMessageDto publisherMessageDto) throws Exception {
        BaseAPIResponse response = pubSubService.publishMessageToTopic(publisherMessageDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(path = Endpoints.SUBSCRIBER_SUBSCRIBE_TOPIC, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseAPIResponse> subscribeTopic(HttpServletRequest servletRequest, @Valid @RequestBody SubscribeDto subscribeDto) throws Exception {
        BaseAPIResponse response = pubSubService.subscribe(subscribeDto);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = Endpoints.SUBSCRIBER_RETRIEVE_MESSAGE, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseAPIResponse> consumeMessage(HttpServletRequest servletRequest, @Valid @RequestBody SubscribeDto subscribeDto) throws Exception {
        BaseAPIResponse response = pubSubService.consumeMessage(subscribeDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(path = Endpoints.SUBSCRIBER_ACK_MESSAGE, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BaseAPIResponse> ackMessage(HttpServletRequest servletRequest, @Valid @RequestBody SubscribeDto subscribeDto) throws Exception {
        BaseAPIResponse response = pubSubService.ackMessage(subscribeDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
