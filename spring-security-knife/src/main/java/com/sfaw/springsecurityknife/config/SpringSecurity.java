package com.sfaw.springsecurityknife.config;

import com.sfaw.springsecurityknife.handler.MyAccessDeniedHander;
import com.sfaw.springsecurityknife.handler.MyAuthDenyEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity
 * security 配置
 * @author ArthurW
 * @version 1.0
 * @date 2023/4/18 15:44
 **/
@Configuration
// 注解内部 @EnableGlobalAuthentication 会开启全局认证
@EnableWebSecurity
// 开启@PreAuthrize、@PostAuthorize注解拦截
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurity {

    @Value("${spring.security.oauth2.ignore-uris}")
    private String[] ignoreUris;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests()
                .antMatchers(this.ignoreUris).permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/docs/**").permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                // 用于设置角色权限
                // .anyRequest().hasAnyAuthority("")
                // .anyRequest().hasAnyRole()
                // .antMatchers().hasAnyRole()
                .and()
                // 注入 authenticationProvider
                .authenticationProvider(authenticationProvider())
                // 添加 jwtAuthFilter 到 UsernamePasswordAuthenticationFilter 前
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // 添加异常处理器
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .authenticationEntryPoint(authEntryPoint())
                .and()
                // 开启跨域
                .cors()
                .and()
                // 关闭csrf
                .csrf().disable()
                // 关闭 formLogin
                .formLogin().disable()
                // 关闭session机制
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        ;
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    private AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public MyAccessDeniedHander accessDeniedHandler() {
        return new MyAccessDeniedHander();
    }

    @Bean
    public MyAuthDenyEntryPoint authEntryPoint() {
        return new MyAuthDenyEntryPoint();
    }

}
