package main.java.com.mercari.merpay.pubsub.dtos.publishers;

import com.mercari.merpay.pubsub.dtos.TopicNameDto;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublisherRegisterDto extends TopicNameDto {
    @NotBlank private String publisherId;
}
