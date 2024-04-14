package jpa.study.hellojpa.shoppingMallProject.service;

import jakarta.persistence.EntityNotFoundException;
import jpa.study.hellojpa.shoppingMallProject.dto.OrderDto;
import jpa.study.hellojpa.shoppingMallProject.entity.Item;
import jpa.study.hellojpa.shoppingMallProject.entity.Member;
import jpa.study.hellojpa.shoppingMallProject.entity.Order;
import jpa.study.hellojpa.shoppingMallProject.entity.OrderItem;
import jpa.study.hellojpa.shoppingMallProject.repository.ItemRepository;
import jpa.study.hellojpa.shoppingMallProject.repository.MemberRepository;
import jpa.study.hellojpa.shoppingMallProject.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
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
}
