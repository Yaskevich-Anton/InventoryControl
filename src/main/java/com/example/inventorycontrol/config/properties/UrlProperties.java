package com.example.inventorycontrol.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "urls")
@Data
public class UrlProperties {
    private Map<String,String> open;
}
