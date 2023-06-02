package com.pubsub.dtos.publishers;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PublisherMessageDto extends PublisherRegisterDto {
    @NotBlank private String message;
}
