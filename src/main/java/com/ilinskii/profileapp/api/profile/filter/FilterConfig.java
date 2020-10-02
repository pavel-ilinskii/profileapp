package com.ilinskii.profileapp.api.profile.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final UniqueEmailFilter uniqueEmailFilter;

    @Bean
    public FilterRegistrationBean<UniqueEmailFilter> uniqueEmailFilterBean() {
        FilterRegistrationBean<UniqueEmailFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(uniqueEmailFilter);
        registrationBean.addUrlPatterns("/profiles/set");
        return registrationBean;
    }
}
