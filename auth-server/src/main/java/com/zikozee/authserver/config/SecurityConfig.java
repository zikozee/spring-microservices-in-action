package com.zikozee.authserver.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationProvider;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.List;
import java.util.function.Consumer;


@Configuration
public class SecurityConfig {


    @Bean
    @Order(1)
    public SecurityFilterChain asSecurityFilterChain(HttpSecurity httpSecurity) throws Exception { // auth server filter chain
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

        httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .authorizationEndpoint(
                        a -> a.authenticationProviders(getAuthorizationEndpointProviders())
                ) // todo remove me, used to customize the authorizationEndpoint
                .oidc(Customizer.withDefaults());

        httpSecurity.exceptionHandling(
                e -> e.authenticationEntryPoint(
                        new LoginUrlAuthenticationEntryPoint("/login")
                )
        ); //force unauthenticated users to login page

        return httpSecurity.build();
    }

    private Consumer<List<AuthenticationProvider>> getAuthorizationEndpointProviders() {
        return providers -> {
            for(AuthenticationProvider p: providers){
                if(p instanceof OAuth2AuthorizationCodeRequestAuthenticationProvider x){
                    x.setAuthenticationValidator(new CustomRedirectUriValidator());
                }
            }
        };
    }

    @Bean
    @Order(2)
    public SecurityFilterChain appSecurityFilterChain(HttpSecurity http) throws Exception { // app filter chain - form login setup authentication
        http.formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(authz -> authz.anyRequest().authenticated());
        return http.build();
    }

    // CustomUserDetailsService is now the bean
    /**
    @Bean
    public UserDetailsService userDetailsService(){
       var uds = User.withUsername("user")
               .password("password")
               .authorities("read")
               .build();

       return new InMemoryUserDetailsManager(uds);
    }
     **/

    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    //Already a bean CustomCLientService
    /**
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
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .accessTokenFormat(OAuth2TokenFormat.REFERENCE) // opaque token, default is no-opaque (SELF_CONTAINED)
                        .authorizationCodeTimeToLive(Duration.ofMinutes(10))
                        .build())
                .build();

        RegisteredClient r2 = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("clienty")
                .clientSecret("secrety")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .redirectUri("https://springone.io/authorized")

                .clientAuthenticationMethods(authMethods -> {
                    authMethods.add(ClientAuthenticationMethod.NONE);
                }) // this means no authentication

                .authorizationGrantTypes(grantTypes -> {
                    grantTypes.add(AuthorizationGrantType.AUTHORIZATION_CODE);
                    grantTypes.add(AuthorizationGrantType.REFRESH_TOKEN);
                })
                .clientSettings(ClientSettings.builder()
//                        .requireAuthorizationConsent(false) // authorization code, device code
                        .requireProofKey(true)// pkce
                        .build())
                .build();

        RegisteredClient r3 = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("client-pass")
                .clientSecret("secret-pass")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .redirectUri("https://springone.io/authorized")

                .clientAuthenticationMethods(authMethods -> {
                    authMethods.add(ClientAuthenticationMethod.CLIENT_SECRET_BASIC);
                }) // this means no authentication

                .authorizationGrantTypes(grantTypes -> {
                    grantTypes.add(AuthorizationGrantType.PASSWORD);
                })
                .build();


        return new InMemoryRegisteredClientRepository(r1, r2, r3);
    }

     **/

    @Bean
    public AuthorizationServerSettings authorizationServerSettings(){
        return AuthorizationServerSettings.builder().build(); // Oauth urls
    }

//    @Bean // read the keys from a config or vault and rotate frequently instead of generating in memory
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

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer(){
        return context -> {
            context.getClaims()
                    .claim("test", "test");

            var authorities = context.getPrincipal().getAuthorities(); // GrantedAuthority

            context.getClaims().claim("authorities", authorities.stream().map(GrantedAuthority::getAuthority).toList()); //List<String
        };


    }
}
