package com.increff.project.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.increff.project.spring.SpringConfig;

@Configuration
@ComponentScan(//
		basePackages = { "com.increff.project" }, //
		excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, value = { SpringConfig.class })//
)
@PropertySources({ //
		@PropertySource(value = "file:./test.properties", ignoreResourceNotFound = true) //
})
public class QaConfig {

}
