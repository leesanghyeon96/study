package jpa.study.hellojpa.shoppingMallProject.entity;

import jakarta.persistence.*;
import jpa.study.hellojpa.shoppingMallProject.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter @Setter
@ToString
@Entity
@Table(name = "item")
public class Item extends BaseEntity{

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm; // 상품 명

    @Column(name = "price", nullable = false)
    private int price; // 상품 가격

    @Column(nullable = false)
    private int stockNumber; // 재고 수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    // 아래의 시간은 BaseEntity적용으로 삭제
    //private LocalDateTime regTime; // 등록 시간
    //private LocalDateTime updateTime; // 수정 시간

}
