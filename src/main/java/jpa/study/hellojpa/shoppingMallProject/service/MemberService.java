package jpa.study.hellojpa.shoppingMallProject.service;

import jpa.study.hellojpa.shoppingMallProject.entity.Member;
import jpa.study.hellojpa.shoppingMallProject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    //UserDetailsService이것은 데이터베이스에서 회원 정보를 가져오는 역할

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    public void validateDuplicateMember(Member member){
        Member findMemberEmail = memberRepository.findByEmail(member.getEmail());
        if(findMemberEmail != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    // UserDetailsService를 상속받아 아래의 함수를 오버라이딩해 사용자의 상세 정보를 불러오는데
    // 스프링 시큐리티에서 인증함.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        //user객체에는 id,pass,Authorization (Role) 3개는 반드시 적용
        //시큐리티에서 인증되면 ROLE_USER,ROLE_ADMIN
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
