package com.badals.shop.config;

import com.badals.shop.aop.tenant.TenantRequestFilter;
import com.badals.shop.security.*;
import com.badals.shop.security.jwt.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Configuration
    @Order(1)
    public static class CustomerWebSecurityConfig extends WebSecurityConfigurerAdapter {

        private final TokenProvider tokenProvider;

        private final CorsFilter corsFilter;
        private final SecurityProblemSupport problemSupport;

        private final TenantRequestFilter tenantRequestFilter;

        private final UserDetailsService userDetailsService;

        public CustomerWebSecurityConfig(TokenProvider tokenProvider, CorsFilter corsFilter, SecurityProblemSupport problemSupport, TenantRequestFilter tenantRequestFilter, @Qualifier("customerDetailsService") UserDetailsService userDetailsService) {
            this.tokenProvider = tokenProvider;
            this.corsFilter = corsFilter;
            this.problemSupport = problemSupport;
            this.tenantRequestFilter = tenantRequestFilter;
            this.userDetailsService = userDetailsService;
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new CustomPasswordEncoder();
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring()
                    .antMatchers(HttpMethod.OPTIONS, "/**")
                    .antMatchers("/swagger-ui/index.html")
                    .antMatchers("/test/**");
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    .csrf()
                    .disable()
                    .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                    .exceptionHandling()
                    .authenticationEntryPoint(problemSupport)
                    .accessDeniedHandler(problemSupport)
                    .and()
                    .headers()
                    .contentSecurityPolicy("default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval'; style-src 'self' 'unsafe-inline' ; img-src 'self' data:")
                    .and()
                    .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                    .and()
                    .featurePolicy("geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; speaker 'none'; fullscreen 'self'; payment 'none'")
                    .and()
                    .frameOptions()
                    .deny()
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/authenticate").permitAll()
                    .antMatchers("/api/authenticate2").permitAll()
                    .antMatchers("/api/store-select").permitAll()
                    .antMatchers("/api/partner-me").authenticated()
                    .antMatchers("/api/register").permitAll()
                    .antMatchers("/api/activate").permitAll()
                    .antMatchers("/api/product/**").permitAll()
                    .antMatchers("/api/report/**").permitAll()
                    .antMatchers("/pricing/**").permitAll()
                    .antMatchers("/graphql").permitAll()
                    .antMatchers("/**/graphql").permitAll()
                    .antMatchers("/detrack").permitAll()
                    .antMatchers("/myus/adfadf32423423423/**").permitAll()
                    .antMatchers("/checkout1432u102398019238092183").permitAll()
                    .antMatchers("/api/users/current").permitAll()
                    .antMatchers("/api/account/reset-password/init").permitAll()
                    .antMatchers("/api/account/reset-password/finish").permitAll()
                    .antMatchers("/api/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .antMatchers("/management/health").permitAll()
                    .antMatchers("/management/info").permitAll()
                    .antMatchers("/management/prometheus").permitAll()
                    .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
                    .and()
                    .httpBasic()
                    .and()
                    .apply(securityConfigurerAdapter());
            // @formatter:on
        }

        @Override
        @Bean
        @Primary
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }


        @Bean(name = "emAuthenticationProvider")
        public AuthenticationProvider emAuthenticationProvider() {
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setPasswordEncoder(passwordEncoder());
            provider.setUserDetailsService(userDetailsService);
            return provider;
        }


        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsService);
            auth.authenticationProvider(emAuthenticationProvider());
        }

        private JWTConfigurer securityConfigurerAdapter() {
            return new JWTConfigurer(tokenProvider);
        }
    }
}
