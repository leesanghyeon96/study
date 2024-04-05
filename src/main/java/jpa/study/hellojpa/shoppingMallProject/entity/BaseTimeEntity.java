package jpa.study.hellojpa.shoppingMallProject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 등록일, 수정일만을 원하는 엔티티에 적용

@EntityListeners(value = {AuditingEntityListener.class}) //Auditing 적용
@MappedSuperclass // 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공
@Getter@Setter
public class BaseTimeEntity {

    @CreatedDate // 엔티티의 생성후 저장될때의 시간 자동 저장
    @Column(updatable = false)
    private LocalDateTime regTime;

    @LastModifiedDate // 값을 변경할 때 시간 자동 저장
    private LocalDateTime updateTime;
}
