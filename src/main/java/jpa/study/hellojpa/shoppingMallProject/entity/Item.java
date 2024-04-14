package jpa.study.hellojpa.shoppingMallProject.entity;

import jakarta.persistence.*;
import jpa.study.hellojpa.shoppingMallProject.constant.ItemSellStatus;
import jpa.study.hellojpa.shoppingMallProject.dto.ItemFormDto;
import jpa.study.hellojpa.shoppingMallProject.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.parameters.P;

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

    // 상품 데이터 업데이트 로직
    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    // 상품을 주문시 재고의 감소 로직
    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;
        if(restStock < 0){
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: " +this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    

}
