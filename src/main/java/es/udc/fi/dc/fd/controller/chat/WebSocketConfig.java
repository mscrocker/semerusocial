package es.udc.fi.dc.fd.controller.chat;

import java.security.Principal;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.DefaultUserDestinationResolver;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.simp.user.UserDestinationResolver;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.DefaultSimpUserRegistry;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import es.udc.fi.dc.fd.jwt.JwtGeneratorImpl;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 50)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	private DefaultSimpUserRegistry userRegistry = new DefaultSimpUserRegistry();
	private DefaultUserDestinationResolver resolver = new DefaultUserDestinationResolver(userRegistry);

	@Bean
	@Primary
	public SimpUserRegistry userRegistry() {
		return userRegistry;
	}

	@Bean
	@Primary
	public UserDestinationResolver userDestinationResolver() {
		return resolver;
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app");
		registry.enableSimpleBroker("/topic/", "/queue");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new ChannelInterceptorAdapter() {

			@SuppressWarnings("unchecked")
			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
				List<String> tokenList = accessor.getNativeHeader("X-Authorization");
				String token = null;
				if (tokenList == null || tokenList.size() < 1)
					return message;
				else {
					token = tokenList.get(0);
					if (token == null)
						return message;
				}
				JwtGeneratorImpl generatorImpl = new JwtGeneratorImpl();
				// validate and convert to a Principal based on your own requirements e.g.
				// authenticationManager.authenticate(JwtAuthentication(token))
				Principal yourAuth = generatorImpl.getInfo(token);

				/*
				 * if (accessor.getMessageType()== SimpMessageType.CONNECT) {
				 * userRegistry.onApplicationEvent(SessionConnectedEvent(this, message,
				 * yourAuth)); } else if (accessor.getMessageType()== SimpMessageType.SUBSCRIBE)
				 * { userRegistry.onApplicationEvent(SessionSubscribeEvent(this, message,
				 * yourAuth)); } else if (accessor.getMessageType()==
				 * SimpMessageType.UNSUBSCRIBE) { userRegistry.onApplicationEvent(new
				 * SessionUnsubscribeEvent(this, message.getPayload(), yourAuth)); } else if
				 * (accessor.getMessageType()== SimpMessageType.DISCONNECT) {
				 * userRegistry.onApplicationEvent(SessionDisconnectEvent(this, message,
				 * accessor.sessionId, CloseStatus.NORMAL)); }
				 */
				if (StompCommand.CONNECT.equals(accessor.getCommand())
						|| StompCommand.SUBSCRIBE.equals(accessor.getCommand())
						|| StompCommand.SEND.equals(accessor.getCommand())) {

					userRegistry
							.onApplicationEvent(new SessionConnectedEvent(this, (Message<byte[]>) message, yourAuth));
					accessor.setUser(yourAuth);
				} else {
					accessor.setUser(yourAuth);
					userRegistry
							.onApplicationEvent(new SessionConnectedEvent(this, (Message<byte[]>) message, yourAuth));

				}

				// not documented anywhere but necessary otherwise NPE in
				// StompSubProtocolHandler!
				accessor.setLeaveMutable(true);
				return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
			}
		});
	}

}
