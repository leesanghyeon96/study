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

    //주문 상품, 수량을 통해 객체를 만드는 메서드
    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item); //주문 상품
        orderItem.setCount(count); //주문 수량
        //현재 가격을 기준으로 세팅(할인을 적용하는 경우는 여기서 고려x)
        orderItem.setOrderPrice(item.getPrice());

        item.removeStock(count); //주문 수량만큼 재고 감소
        return orderItem;
    }

    //상품의 총 가격
    public int getTotalPrice(){
        return orderPrice*count;
    }

    //취소시 주문 수량만큼 재고를 증가시키는 메서드
    public void cancel(){
        this.getItem().addStock(count);
    }
}
