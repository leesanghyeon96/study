package jpa.study.hellojpa.shoppingMallProject.service;

import jpa.study.hellojpa.shoppingMallProject.dto.ItemFormDto;
import jpa.study.hellojpa.shoppingMallProject.entity.Item;
import jpa.study.hellojpa.shoppingMallProject.entity.ItemImg;
import jpa.study.hellojpa.shoppingMallProject.repository.ItemImgRepository;
import jpa.study.hellojpa.shoppingMallProject.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

}
