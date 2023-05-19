package com.sfaw.springsecurityknife.config;

import com.sfaw.springsecurityknife.handler.MyAccessDeniedHandler;
import com.sfaw.springsecurityknife.handler.MyAuthDenyEntryPoint;
import com.sfaw.springsecurityknife.handler.MyPermissionEvaluator;
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
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
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

    @Autowired
    private MyPermissionEvaluator myPermissionEvaluator;

    /**
     * 静态文件放行,这种方式不会走过滤器链路
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/css/**", "/js/**", "/index.html", "/img/**", "/fonts/**", "/favicon.ico", "/verifyCode", "/**.html");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeRequests()
                // 白名单配置
                .antMatchers(this.ignoreUris).permitAll()
                // swagger 页面地址放开
                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/webjars/**", "/doc.html").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/docs/**").permitAll()
                // 关于登录的一些接口
                .antMatchers("/tologin", "/loginpage.html", "/app/getApp", "/app/addApp").permitAll()
                .anyRequest().authenticated()
                // 添加自己的 permission 处理器
                .expressionHandler(defaultWebSecurityExpressionHandler())
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
                //.and()
                // 开启登录 formLogin
                .formLogin() //.disable()
                // 登录页面参数 默认username,password
                .usernameParameter("username")
                .passwordParameter("password")
                // 登录页面
                .loginPage("/loginpage.html")
                // 登录接口  不配置，会由前端发起调用，配置后，就会在 UsernamePasswordAuthenticationFilter 中进行用户名和密码校验，可以不真实存在的地址
                .loginProcessingUrl("/tologin")
                // 登录成功默认跳转页面
                .defaultSuccessUrl("/userinfo.html")
                // .defaultSuccessUrl("/doc.html", true)
                .permitAll()

                .and()
                // 关闭session机制
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

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
    public MyAccessDeniedHandler accessDeniedHandler() {
        return new MyAccessDeniedHandler();
    }

    @Bean
    public MyAuthDenyEntryPoint authEntryPoint() {
        return new MyAuthDenyEntryPoint();
    }

    @Bean
    public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
        defaultWebSecurityExpressionHandler.setPermissionEvaluator(myPermissionEvaluator);
        return defaultWebSecurityExpressionHandler;
    }
}
