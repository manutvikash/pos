package com.increff.project.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan("com.increff.project")
@PropertySources({ @PropertySource(value = "file:./project.properties", ignoreResourceNotFound = true) })
public class SpringConfig {

}
