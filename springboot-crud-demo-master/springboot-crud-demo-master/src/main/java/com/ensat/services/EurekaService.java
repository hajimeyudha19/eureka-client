package com.ensat.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

@Service
public class EurekaService {
    
    @Autowired
    private DiscoveryClient discoveryClient;

    public String getEurekaServerIp() {
        List<String> services = discoveryClient.getServices();
        // Assuming the service name of Eureka server is "eureka-server"
        if (services.contains("eureka-server")) {
            return discoveryClient.getInstances("eureka-server")
                    .stream()
                    .findFirst()
                    .map(instance -> instance.getHost() + ":" + instance.getPort())
                    .orElse("Unknown");
        }
        return "Unknown";
    }
}