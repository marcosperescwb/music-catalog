package com.music.catalog.infra.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefixo para mensagens enviadas do servidor para o cliente
        config.enableSimpleBroker("/topic");
        // Prefixo para mensagens enviadas do cliente para o servidor (não usaremos aqui, mas é boa prática)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint de conexão (Handshake)
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Permite qualquer origem (CORS)
                .withSockJS(); // Habilita fallback para navegadores antigos
    }
}