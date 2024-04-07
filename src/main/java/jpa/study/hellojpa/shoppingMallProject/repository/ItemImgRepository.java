package jpa.study.hellojpa.shoppingMallProject.repository;

import jpa.study.hellojpa.shoppingMallProject.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    //select * from item_img where item_id = ? order by id asc, 상품 이미지 아이디 오름차순
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

}
