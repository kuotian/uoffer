package com.hxxzt.recruitment.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "penint")
public class PenintProperties {
    private ShiroProperties shiro = new ShiroProperties();
    private ResourceProperties resourcePath = new ResourceProperties();
    private boolean openAopLog = true;
}
