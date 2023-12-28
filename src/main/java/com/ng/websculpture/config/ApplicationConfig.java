package com.ng.websculpture.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Author: Idris Ishaq
 * @Date: 28 Dec, 2023
 */

@Configuration
@Import({RouterConfig.class})
@ComponentScan({"com.ng.websculpture.*"})
public class ApplicationConfig {

}
