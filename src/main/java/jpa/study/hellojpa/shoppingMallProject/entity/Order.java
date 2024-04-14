package jpa.study.hellojpa.shoppingMallProject.entity;

import jakarta.persistence.*;
import jpa.study.hellojpa.shoppingMallProject.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")// 정렬 키워드 order이 있으므로 orders로 지정
public class Order extends BaseEntity{

    //주문 엔티티

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 한명이 여러번 주문 가능
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;  // 주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;  // 주문 상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL,
                orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>(); // 하나의 주문이 여러 상품을 가진다.
    //orphanRemoval : 고아객체 제거 옵션 (이를 사용하기 위해선 참조하는 곳이 하나일 때만 사용해야한다.)
    //고아객체 : 부모 엔티티와 연관 관계가 끊어진 자식 엔티티

    // BaseEntity 적용으로 삭제
    //private LocalDateTime regTime;
    //private LocalDateTime updateTime;


    /*
        주문 관련 메서드
     */
    //OrderItem에서 생성한 주문 "상품" 객체를 바탕으로 주문 객체 생성 메서드
    public void addOrderItem(OrderItem orderItem){ //주문 상품의 정보
        orderItems.add(orderItem); //orderItem을 orderItems에 추가
        orderItem.setOrder(this); //양방향 참조 관계이므로 orderItem 객체에도 order 객체 세팅
    }

    //주문 메서드 생성
    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member); //주문한 회원 저장
        for (OrderItem orderItem : orderItemList) { //여러개의 주문 상품을 받을 수 있도록
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER); //주문 상태 세팅
        order.setOrderDate(LocalDateTime.now()); //주문 시간 세팅
        return order;
    }

    //주문 총 가격 메서드
    public int getTotalPrice(){
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    //주문 취소시 주문 수량을 상품의 재고에 더해주는 로직과
    //주문 상태를 취소 상태로 변경하는 메서드
    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
}
