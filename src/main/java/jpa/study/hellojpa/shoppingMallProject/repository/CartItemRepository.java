package jpa.study.hellojpa.shoppingMallProject.repository;

import jpa.study.hellojpa.shoppingMallProject.dto.CartDetailDto;
import jpa.study.hellojpa.shoppingMallProject.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
장바구니에 들어갈 상품을 저장하거나 조회하기 위해 생성
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    //카트 아이디와 상품 아이디를 이용해 상품이 장바구니에 들어있는지 조회
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    //장바구니 페이지에 전달할 CartDetailDto 리스트를 쿼리 하나로 조회하는 JPQL 문 작성
    @Query("select new jpa.study.hellojpa.shoppingMallProject.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repimgYn = 'Y' " +
            "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);


}
