package main.java.com.mercari.merpay.pubsub.commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseAPIResponse {
    protected String status;
    protected String message;
}
