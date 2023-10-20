package com.mmg.loadBalance;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.util.pattern.PathPatternParser;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther: fan
 * @Date: 2022/7/4
 * @Description: 服务选择
 */
@Slf4j
public class CustomLoadBalancer implements ILoadBalancer {
    private final DiscoveryClient discoveryClient;
    private final AtomicInteger nextServerCyclicCounter;

    public CustomLoadBalancer(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
        nextServerCyclicCounter = new AtomicInteger(0);
    }

    private static final PathPatternParser pathPatternParser = new PathPatternParser();

    /**
     * 根据serviceId 筛选可用服务
     *
     * @param serviceId 服务ID
     * @param request   当前请求
     */
    @Override
    public ServiceInstance choose(String serviceId, ServerHttpRequest request) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        //注册中心无实例 抛出异常
        if (instances.isEmpty()) {
            log.warn("No instance available for {}", serviceId);
            throw new NotFoundException("No instance available for " + serviceId);
        }
        //默认转发的服务实例
        ServiceInstance defaultServiceInstance = instances.get(incrementAndGetModulo(instances.size()));
        InetSocketAddress remoteAddress = request.getRemoteAddress();
        log.info("请求来源：{}", remoteAddress == null ? "Unknown" : remoteAddress.getHostString());
        log.info("请求的地址：{}{}", defaultServiceInstance.getUri(), request.getURI().getPath());
        log.info("转发的服务：{}", JSON.toJSONString(defaultServiceInstance));
        return defaultServiceInstance;
    }

    private int incrementAndGetModulo(int modulo) {
        for (; ; ) {
            int current = nextServerCyclicCounter.get();
            int next = (current + 1) % modulo;
            if (nextServerCyclicCounter.compareAndSet(current, next))
                return next;
        }
    }

    private boolean matchUri(String path) {
        return pathPatternParser.parse("/screen/**").matches(PathContainer.parsePath(path));
    }
}
