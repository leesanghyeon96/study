package jpa.study.hellojpa.shoppingMallProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // Jpa의 Auditing기능 활성화
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider(){ // AuditorAware을 빈으로 등록
        return new AuditorAwareImpl();
    }
}
