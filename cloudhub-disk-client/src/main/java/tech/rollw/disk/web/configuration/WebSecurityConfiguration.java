/*
 * Copyright (C) 2023 RollW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.rollw.disk.web.configuration;

import tech.rollw.disk.web.configuration.compenent.WebDelegateSecurityHandler;
import tech.rollw.disk.web.configuration.filter.ContextInitializeFilter;
import tech.rollw.disk.web.configuration.filter.CorsConfigFilter;
import tech.rollw.disk.web.configuration.filter.OperateLogFilter;
import tech.rollw.disk.web.configuration.filter.TokenAuthenticationFilter;
import tech.rollw.disk.web.domain.authentication.token.AuthenticationTokenService;
import tech.rollw.disk.web.domain.user.UserDetailsService;
import tech.rollw.disk.web.domain.user.service.UserSignatureProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * @author RollW
 */
@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class WebSecurityConfiguration {
    private final UserDetailsService userDetailsService;

    public WebSecurityConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security,
                                                   CorsConfigFilter corsConfigFilter,
                                                   TokenAuthenticationFilter tokenAuthenticationFilter,
                                                   OperateLogFilter operateLogFilter,
                                                   ContextInitializeFilter contextInitializeFilter,
                                                   AuthenticationEntryPoint authenticationEntryPoint,
                                                   AccessDeniedHandler accessDeniedHandler) throws Exception {


        security.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                // TODO: customize accessDecisionManager
                //.accessDecisionManager(accessDecisionManager())
                .antMatchers("/api/{version}/auth/token/**").permitAll()
                .antMatchers("/api/{version}/quickfire/**").permitAll()
                .antMatchers("/api/{version}/shares/**").permitAll()
                .antMatchers("/api/{version}/admin/**").hasRole("ADMIN")
                .antMatchers("/api/{version}/common/**").permitAll()
                .antMatchers("/api/{version}/{ownerType}/{ownerId}/disk/**").hasRole("USER")
                .antMatchers("/api/{version}/user/login/logs").hasRole("USER")
                .antMatchers("/api/{version}/user/login/**").permitAll()
                .antMatchers("/api/{version}/user/register/**").permitAll()
                .antMatchers("/api/{version}/user/logout/**").permitAll()

                .antMatchers("/**").hasRole("USER")
                .anyRequest().permitAll();
        security.userDetailsService(userDetailsService);

        security.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        security.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        security.addFilterBefore(tokenAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);
        security.addFilterBefore(corsConfigFilter,
                TokenAuthenticationFilter.class);
        security.addFilterBefore(contextInitializeFilter,
                TokenAuthenticationFilter.class);
        security.addFilterAfter(operateLogFilter,
                TokenAuthenticationFilter.class);
        return security.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .antMatchers("/static/img/**");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigFilter corsConfigFilter(
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        return new CorsConfigFilter(resolver);
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(
            AuthenticationTokenService authenticationTokenService,
            UserSignatureProvider userSignatureProvider) {
        return new TokenAuthenticationFilter(
                authenticationTokenService,
                userDetailsService,
                userSignatureProvider
        );
    }

    @Bean
    public WebDelegateSecurityHandler webDelegateSecurityHandler(
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        return new WebDelegateSecurityHandler(resolver);
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // removes the "ROLE_" prefix
    }
}
