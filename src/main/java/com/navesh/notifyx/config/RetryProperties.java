package com.navesh.notifyx.config;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Component
@Validated
@ConfigurationProperties(prefix = "notification.retry")
public class RetryProperties {

    @Min(1)
    private int maxAttempts = 3;

    @Min(1000)
    private long fixedDelay = 60000;
}