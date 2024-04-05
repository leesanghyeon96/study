package jpa.study.hellojpa.shoppingMallProject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter@Setter
public class OrderItem extends BaseEntity{

    //주문 아이템 엔티티

    @Id@GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //fetch = FetchType.LAZY -> 지연로딩
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;  // 주문 가격

    private int count;  // 수량

    //BaseEntity 적용으로 삭제
    //private LocalDateTime regTime;
    //private LocalDateTime updateTime;
}
