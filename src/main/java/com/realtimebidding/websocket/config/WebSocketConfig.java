package com.realtimebidding.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	/*
	 * url for request -> /app/rt-auction/
	 * url to subscribe -> /rt-product/{auctionId}
	 */
	
	@Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/rt-product");
        registry.setApplicationDestinationPrefixes("/app");
    }
	
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/rt-auction").setAllowedOrigins("*").withSockJS();
    }
}