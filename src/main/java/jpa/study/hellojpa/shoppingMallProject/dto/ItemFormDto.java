package jpa.study.hellojpa.shoppingMallProject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jpa.study.hellojpa.shoppingMallProject.constant.ItemSellStatus;
import jpa.study.hellojpa.shoppingMallProject.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter@Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수입니다.")
    private String ItemNm;

    @NotNull(message = "가격은 필수입니다.")
    private Integer price;

    @NotBlank(message = "이름은 필수입니다.")
    private String itemDetail;

    @NotNull(message = "재고를 입력해 주세요.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); // 이미지 정보 저장 리스트

    private List<Long> itemImgIds = new ArrayList<>(); // 상품의 이미지 아이디 저장 리스트

    private static ModelMapper modelMapper = new ModelMapper();

    // modelMapper를 사용해 객체와dto간의 데이터 복사 후 객체 반환 메서드
    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }
    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class);
    }
}
