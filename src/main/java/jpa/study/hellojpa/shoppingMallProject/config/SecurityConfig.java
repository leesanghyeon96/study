package jpa.study.hellojpa.shoppingMallProject.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                //h2 db
                .csrf((csrf) -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
                .headers((headers) -> headers
                        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)))

                //로그인
                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/members/login")
                                .loginProcessingUrl("/members/login") // 로그인 폼이 제출되는 URL
                                .defaultSuccessUrl("/")
                                .usernameParameter("email")
                                .failureUrl("/members/login/error"))
                .logout(logout ->
                        logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                                .logoutSuccessUrl("/"))
                //권한
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                        //db의 role에 ADMIN이면 hasAuthority, ROLE_ADMIN이면 hasRole
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                //인증되지 않은자
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                );



        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
