/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurationSupport;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * A {@link WebSocketMessageBrokerConfigurationSupport} extension that detects
 * beans of type {@link WebSocketMessageBrokerConfigurer} and delegates to all
 * of them allowing callback style customization of the configuration provided
 * in {@link WebSocketMessageBrokerConfigurationSupport}.
 *
 * <p>
 * This class is typically imported via {@link EnableWebSocketMessageBroker}.
 *
 * @author Rossen Stoyanchev
 * @since 4.0
 */
@Configuration(proxyBeanMethods = false)
public class DelegatingWebSocketMessageBrokerConfiguration extends WebSocketMessageBrokerConfigurationSupport {

    private final List<WebSocketMessageBrokerConfigurer> configurers = new ArrayList<>();

    @Autowired(required = false)
    public void setConfigurers(List<WebSocketMessageBrokerConfigurer> configurers) {
        if (!CollectionUtils.isEmpty(configurers)) {
            this.configurers.addAll(configurers);
        }
    }

    @Override
    protected void registerStompEndpoints(StompEndpointRegistry registry) {
        for (WebSocketMessageBrokerConfigurer configurer : this.configurers) {
            configurer.registerStompEndpoints(registry);
        }
    }

    @Override
    protected void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        for (WebSocketMessageBrokerConfigurer configurer : this.configurers) {
            configurer.configureWebSocketTransport(registration);
        }
    }

    @Override
    protected void configureClientInboundChannel(ChannelRegistration registration) {
        for (WebSocketMessageBrokerConfigurer configurer : this.configurers) {
            configurer.configureClientInboundChannel(registration);
        }
    }

    @Override
    protected void configureClientOutboundChannel(ChannelRegistration registration) {
        for (WebSocketMessageBrokerConfigurer configurer : this.configurers) {
            configurer.configureClientOutboundChannel(registration);
        }
    }

    @Override
    protected void configureMessageBroker(MessageBrokerRegistry registry) {
        for (WebSocketMessageBrokerConfigurer configurer : this.configurers) {
            configurer.configureMessageBroker(registry);
        }
    }

}
