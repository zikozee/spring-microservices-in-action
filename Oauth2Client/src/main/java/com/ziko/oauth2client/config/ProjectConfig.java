package com.ziko.oauth2client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author : Ezekiel Eromosei
 * @code @created : 24 Nov, 2023
 */

@Configuration
public class ProjectConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.oauth2Client(Customizer.withDefaults());

        http.authorizeHttpRequests(authz -> authz.anyRequest().permitAll());

        return http.build();
    }

    @Bean
    public OAuth2AuthorizedClientManager auth2AuthorizedClientManager(
            ClientRegistrationRepository repository,
//            OAuth2AuthorizedClientRepository auth2AuthorizedClientRepository, // option 2
            OAuth2AuthorizedClientService oAuth2AuthorizedClientService
    ){
        OAuth2AuthorizedClientProvider provider = OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode() // not needed in this case, only if we are connecting via authorization code
                .refreshToken() // not needed in this case, only if we using refreshToken
                .clientCredentials()
                .build();

        /* introspect OAuth2AuthorizedClientManager to see implementation so you'ld know which one to use*/
        // option 2
//        DefaultOAuth2AuthorizedClientManager clientManager = new DefaultOAuth2AuthorizedClientManager(repository, auth2AuthorizedClientRepository);
//        clientManager.setAuthorizedClientProvider(provider);

        AuthorizedClientServiceOAuth2AuthorizedClientManager clientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(repository, oAuth2AuthorizedClientService);
        clientManager.setAuthorizedClientProvider(provider);

        return clientManager;
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(){ // if there are multiple auth server, you just need to create multiple client registration with the respective config
        ClientRegistration clientRegistration = ClientRegistration
                .withRegistrationId("oauthClient")
                .clientId("client")
                .clientSecret("secret")
//                .clientId("clientopaque")
//                .clientSecret("secretopaque")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .tokenUri("http://localhost:8072/auth/oauth2/token")  // from http://localhost:8072/auth/.well-known/openid-configuration >>token_endpoint
                .scope(OidcScopes.OPENID)
                .build();

        return new InMemoryClientRegistrationRepository(clientRegistration); // load from DB, give the user an id or so then when that user pass that id, call its details from the db
    }
}
