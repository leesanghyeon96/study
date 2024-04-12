package jpa.study.hellojpa.shoppingMallProject.repository;

import jpa.study.hellojpa.shoppingMallProject.dto.ItemSearchDto;
import jpa.study.hellojpa.shoppingMallProject.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// 인터페이스로 선언만 되어있고 이를 구현하는 클래스를 따로 생성 (ItemRepositoryCustomImpl)
public interface ItemRepositoryCustom {

    //관리자 페이지에서 처리하는 검색어 구현
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

}
