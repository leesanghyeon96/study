package jpa.study.hellojpa.shoppingMallProject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
public class CartItem extends BaseEntity{

    // 장바구니 아이템 엔티티

    @Id
    @GeneratedValue
    @Column(name = "cart_item")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;  // 장바구니에 여러개의 상품

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;  // 상품의 정보

    private int count;  // 같은 상품의 개수


    //장바구니에 담을 상품 아이템 생성 메서드
    public static CartItem createCartItem(Cart cart, Item item, int count){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }
    //장바구니 상품 수량증가
    public void addCount(int count){
        this.count += count;
    }

}
