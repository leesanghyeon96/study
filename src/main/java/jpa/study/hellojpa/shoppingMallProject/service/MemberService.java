package jpa.study.hellojpa.shoppingMallProject.service;

import jpa.study.hellojpa.shoppingMallProject.entity.Member;
import jpa.study.hellojpa.shoppingMallProject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

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

}
