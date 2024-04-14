package jpa.study.hellojpa.shoppingMallProject.repository;

import jpa.study.hellojpa.shoppingMallProject.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    //select * from item_img where item_id = ? order by id asc, 상품 이미지 아이디 오름차순
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    //구매 이력 페이지에서 주문 상품의 대표 이미지를 보여주기 위해 상품의 대표 이미지를 찾는 쿼리 메서드를 추가
    ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);

}
