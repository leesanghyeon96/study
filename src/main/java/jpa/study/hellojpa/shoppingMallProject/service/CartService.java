package jpa.study.hellojpa.shoppingMallProject.service;

import jakarta.persistence.EntityNotFoundException;
import jpa.study.hellojpa.shoppingMallProject.dto.CartItemDto;
import jpa.study.hellojpa.shoppingMallProject.entity.Cart;
import jpa.study.hellojpa.shoppingMallProject.entity.CartItem;
import jpa.study.hellojpa.shoppingMallProject.entity.Item;
import jpa.study.hellojpa.shoppingMallProject.entity.Member;
import jpa.study.hellojpa.shoppingMallProject.repository.CartItemRepository;
import jpa.study.hellojpa.shoppingMallProject.repository.CartRepository;
import jpa.study.hellojpa.shoppingMallProject.repository.ItemRepository;
import jpa.study.hellojpa.shoppingMallProject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
장바구니 로직
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    //장바구니에 상품을 담는 로직
    public Long addCart(CartItemDto cartItemDto, String email){

        //장바구니에 담을 상품 엔티티 조회
        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        //현재 로그인한 회원 엔티티를 조회
        Member member = memberRepository.findByEmail(email);

        //현재 로그인한 회원의 장바구니 엔티티 조회
        Cart cart = cartRepository.findByMemberId(member.getId());
        //상품을 처음으로 장바구니에 담을 경우 해당 회원의 장바구니 엔티티 생성
        if(cart==null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        //현재 상품이 장바구니에 이미 들어가 있는지 조회
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if(savedCartItem != null){
            //장바구니에 이미 들어가 있으면 기존 수량에 장바구니에 담을 수량만큼 더해주기
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        }else{
            //장바구니, 상품 엔티티와 담을 수량을 이용해 CartItem 엔티티 생성
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            //상품 저장
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

}
