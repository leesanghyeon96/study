package jpa.study.hellojpa.shoppingMallProject.dto;

import jpa.study.hellojpa.shoppingMallProject.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class ItemSearchDto {
    // 검색 Dto
    private String searchDateType; //현재 기준 상품 등록일 검색
    private ItemSellStatus searchSellStatus; // 상품 판매 상태
    private String searchBy; //검색 유형 : 상품명(itemNm), 상품 등록자(createBy)
    private String searchQuery = ""; //조회할 검색어 저장 변수

}
