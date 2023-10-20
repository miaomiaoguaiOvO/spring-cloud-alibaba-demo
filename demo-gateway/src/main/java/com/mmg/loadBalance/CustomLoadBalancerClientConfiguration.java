package com.mmg.loadBalance;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerProperties;
import org.springframework.cloud.gateway.config.GatewayReactiveLoadBalancerClientAutoConfiguration;
import org.springframework.cloud.gateway.filter.ReactiveLoadBalancerClientFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: fan
 * @Date: 2022/7/4
 * @Description:
 */
@Configuration
@EnableConfigurationProperties(GatewayLoadBalancerProperties.class)
@AutoConfigureBefore(GatewayReactiveLoadBalancerClientAutoConfiguration.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
public class CustomLoadBalancerClientConfiguration {

    @Bean
    public ILoadBalancer getCustomLoadBalancer(DiscoveryClient discoveryClient) {
        return new CustomLoadBalancer(discoveryClient);
    }

    @Bean
    public ReactiveLoadBalancerClientFilter gatewayLoadBalancerClientFilter(GatewayLoadBalancerProperties properties, ILoadBalancer loadBalancer) {
        return new CustomReactiveLoadBalancerClientFilter(properties, loadBalancer);
    }
}
