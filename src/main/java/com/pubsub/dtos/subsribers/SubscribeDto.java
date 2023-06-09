package com.pubsub.dtos.subsribers;

import com.pubsub.dtos.TopicNameDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeDto extends TopicNameDto {
    @NotBlank private String subscriberId;
}
