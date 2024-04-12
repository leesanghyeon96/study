package jpa.study.hellojpa.shoppingMallProject.service;

import jakarta.persistence.EntityNotFoundException;
import jpa.study.hellojpa.shoppingMallProject.dto.ItemFormDto;
import jpa.study.hellojpa.shoppingMallProject.dto.ItemImgDto;
import jpa.study.hellojpa.shoppingMallProject.dto.ItemSearchDto;
import jpa.study.hellojpa.shoppingMallProject.entity.Item;
import jpa.study.hellojpa.shoppingMallProject.entity.ItemImg;
import jpa.study.hellojpa.shoppingMallProject.repository.ItemImgRepository;
import jpa.study.hellojpa.shoppingMallProject.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        // 상품 등록
        // client 에서 넘긴 itemFormDto의 값을 getter 로 끄집어내서
        // item Entity 클래스의 Setter로 주입후 Repository 의 save 메소드로 장
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);			//Foreign Key 설정

            if(i == 0)						// 첫 번째 이미지는 Y (대표 이미지 설정)
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

    //등록된 상품을 불러오는 메서드 생성
    @Transactional(readOnly = true)
    public ItemFormDto getItemDtl(Long itemId){
        //이미지 조회
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        //조회한 이미지 리스트에 추가
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        // item 테이블에서 정보를 가지고 와서 dto 에 주입
        //itemID 값이 존재하면 item Entity 클래스에 저장이되고 없으면 예외를 발생 시키도록 함.
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // itemFormDTO = item + item_img 테이블의 모두 저장해서 클라이언트로 전송해주는 DTO
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        return itemFormDto;
    }


    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i));
        }

        return item.getId();
    }

    //상품 데이터를 조회하는 메서드
    @Transactional(readOnly = true) //데이터의 수정이 일어나지 않으므로
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        //상품 조회 조건과 페이지 정보
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

}
