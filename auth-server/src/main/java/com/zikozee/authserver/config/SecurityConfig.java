package com.zikozee.authserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.UUID;


@Configuration
public class SecurityConfig {


    @Bean
    @Order(1)
    public SecurityFilterChain asSecurityFilterChain(HttpSecurity httpSecurity) throws Exception { // auth server filter chain
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

        httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());

        httpSecurity.exceptionHandling(
                e -> e.authenticationEntryPoint(
                        new LoginUrlAuthenticationEntryPoint("/login")
                )
        ); //force unauthenticated users to login page

        return httpSecurity.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception { // app filter chain
        http.formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(authz -> authz.anyRequest().authenticated());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
       var uds = User.withUsername("user")
               .password("password")
               .authorities("read")
               .build();

       return new InMemoryUserDetailsManager(uds);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(){

        RegisteredClient r1 = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("client")
                .clientSecret("secret")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .redirectUri("https://springone.io/authorized")
//                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // this means you have to authenticate as basic auth as client_id and secret
                .clientAuthenticationMethods(authMethods -> {
                    authMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                }) // this means you have to authenticate as basic auth as client_id and secret
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantTypes(grantTypes -> {
                    grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    grantTypes.add(AuthorizationGrantType.PASSWORD);
                    grantTypes.add(AuthorizationGrantType.CLIENT_CREDENTIALS);
                })
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .clientSettings(ClientSettings.builder()
//                        .requireAuthorizationConsent(false) // authorization code, device code
//                        .requireProofKey(true)// pkce
//                        .build())
                .build();

        RegisteredClient r2 = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("clienty")
                .clientSecret("secrety")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .redirectUri("https://springone.io/authorized")

                .clientAuthenticationMethods(authMethods -> {
                    authMethods.add(ClientAuthenticationMethod.NONE);
                }) // this means you have to authenticate as basic auth as client_id and secret

                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//                .clientSettings(ClientSettings.builder()
//                        .requireAuthorizationConsent(false) // authorization code, device code
//                        .requireProofKey(true)// pkce
//                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(r1, r2);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(){
        return AuthorizationServerSettings.builder().build(); // Oauth urls
    }

//    @Bean // not necessary with spring boot
//    public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
//        KeyPairGenerator kg = KeyPairGenerator.getInstance("RSA");
//        kg.initialize(2048);
//        KeyPair keyPair = kg.generateKeyPair();
//
//        RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
//        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
//
//
//        RSAKey key = new RSAKey.Builder(publicKey)
//                .privateKey(privateKey)
//                .keyID(UUID.randomUUID().toString())
//                .build();
//
//        JWKSet set = new JWKSet(key);
//        return new ImmutableJWKSet<>(set);
//    }
}
