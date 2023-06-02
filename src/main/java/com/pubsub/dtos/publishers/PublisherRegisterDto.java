package com.pubsub.dtos.publishers;

import com.pubsub.dtos.TopicNameDto;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublisherRegisterDto extends TopicNameDto {
    @NotBlank private String publisherId;
}
