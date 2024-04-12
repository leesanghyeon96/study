package jpa.study.hellojpa.shoppingMallProject.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

    /*
        메인 페이지에 상품을 보여줄때 사용할 Dto
     */
@Getter
@Setter
public class MainItemDto {

    private Long id;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;

    //  QueryProjection를 사용하려면 QDto파일과 QMainItemDto가 생성되어야한다.(generated하위..)
    @QueryProjection  //Querydsl로 결과 조회시 MainItemDto객체로 바로 받아올 수 있는 어노테이션
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price) {
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}
