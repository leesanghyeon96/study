package jpa.study.hellojpa.shoppingMallProject.dto;

import jpa.study.hellojpa.shoppingMallProject.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

/*
    주문 이력 조회 Dto
    상품 정보를 담을 클래스
    -> 주문 정보를 담을 OrderHistDto 클래스 생성
 */
@Getter@Setter
public class OrderItemDto {

    private String itemNm; //상품명
    private int count; //주문 수량

    private int orderPrice; //주문 금액
    private String imgUrl; //상품 이미지 경로

    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

}
