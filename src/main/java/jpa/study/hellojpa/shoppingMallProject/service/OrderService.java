package jpa.study.hellojpa.shoppingMallProject.service;

import jakarta.persistence.EntityNotFoundException;
import jpa.study.hellojpa.shoppingMallProject.dto.OrderDto;
import jpa.study.hellojpa.shoppingMallProject.dto.OrderHistDto;
import jpa.study.hellojpa.shoppingMallProject.dto.OrderItemDto;
import jpa.study.hellojpa.shoppingMallProject.entity.*;
import jpa.study.hellojpa.shoppingMallProject.repository.ItemImgRepository;
import jpa.study.hellojpa.shoppingMallProject.repository.ItemRepository;
import jpa.study.hellojpa.shoppingMallProject.repository.MemberRepository;
import jpa.study.hellojpa.shoppingMallProject.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/*
    주문 로직 서비스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemImgRepository itemImgRepository; //주문 이력 이미지 조회를 위해
    
    public Long order(OrderDto orderDto, String email){
        //주문할 상품을 조회
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email); //현재 로그인한 사용자의 이메일

        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount()); //주문 상품 엔티티 생성
        orderItemList.add(orderItem);

        Order order = Order.createOrder(member, orderItemList); //주문 엔티티 생성
        orderRepository.save(order); //주문 엔티티 저장

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable){

        List<Order> orders = orderRepository.findOrders(email, pageable); //아이디와 페이징 조건으로 목록 조회
        Long totalCount = orderRepository.countOrder(email); //주문 총 개수

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        //주문 리스트를 순회하며 구매이력 페이지에 전달할 DTO 생성
        for (Order order : orders) {
            OrderHistDto orderHistDto = new OrderHistDto(order);
            List<OrderItem> orderItems = order.getOrderItems();

            for (OrderItem orderItem : orderItems) {
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepimgYn
                        (orderItem.getItem().getId(), "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl()); //대표 이미지
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }

        //페이지 구현 객체를 생성하여 반환
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }
}
