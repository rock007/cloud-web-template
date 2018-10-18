package org.cloud.webtemplate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource({"classpath:config/security/applicationContext-shiro.xml"})
public class Config {


}
