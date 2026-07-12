package com.navesh.notifyx.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "provider")
public class ProviderProperties {

    private String name;

}
