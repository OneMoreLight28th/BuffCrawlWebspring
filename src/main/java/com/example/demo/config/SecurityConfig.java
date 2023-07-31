package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 禁用CSRF保护
                .authorizeRequests()
                .antMatchers("/login").permitAll() // 登录页面允许所有用户访问
                .antMatchers("/user/**").hasRole("USER") // 用户页面需要用户角色才能访问
//                .anyRequest().authenticated()   其他所有请求都需要认证
                .and()
                .formLogin()
                .loginPage("/login") // 登录页面的URL
                .defaultSuccessUrl("/Skin") // 登录成功后默认跳转的URL，此处跳转到用户页面
                .and()
                .logout()
                .logoutUrl("/logout") // 登出URL
                .logoutSuccessUrl("/Skin") // 登出成功后跳转的URL，此处跳转回登录页面
                .invalidateHttpSession(true) // 清除session
                .deleteCookies("JSESSIONID"); // 删除JSESSIONID cookie
    }
}
