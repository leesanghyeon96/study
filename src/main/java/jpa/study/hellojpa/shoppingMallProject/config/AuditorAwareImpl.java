package jpa.study.hellojpa.shoppingMallProject.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

// Spring Data Jpa에서 Auditing 기능을 사용해 엔티티가 저장, 수정될 때 자동으로 등록.수정.일, 등록.수정자를 입력
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";

        if(authentication != null){
            userId = authentication.getName();  // 현재 로그인한 사용자의 정보를 조회해 등록.수정자로 지정
        }

        return Optional.of(userId);
        // 이후 이 기능을 사용하기 위해 Config 파일을 생성
    }
}
