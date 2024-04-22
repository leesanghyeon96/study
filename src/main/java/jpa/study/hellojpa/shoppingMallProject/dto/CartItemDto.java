package jpa.study.hellojpa.shoppingMallProject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

/*
상세 페이지에서 장바구니에 담을
상품의 id, 상품의 개수를 담는 dto
 */

@Getter@Setter
public class CartItemDto {

    @NotNull(message = "상품 아이디는 필수 입력 값 입니다.")
    private Long itemId;

    @Min(value = 1, message = "최소 1개 이상 담아주세요.")
    private int count;
}
