package jpa.study.hellojpa.shoppingMallProject.service;

import jakarta.persistence.EntityNotFoundException;
import jpa.study.hellojpa.shoppingMallProject.entity.ItemImg;
import jpa.study.hellojpa.shoppingMallProject.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {
    // 상품 이미지를 업로드, 정보 저장

    @Value("${itemImgLocation}")	// application.properites : itemImgLocation=C:/shop/item
    private String itemImgLocation;	// C:/shop/item

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;


    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{

        // oriImgNanme : MultipartFile에서 넘어오는 원본이미지 이름을
        String oriImgName = itemImgFile.getOriginalFilename();		// 원본 이미지 파일 이름
        String imgName = "";										// 서버에 저장할 이미지 이름
        String imgUrl = "";											// 전체 이미지

        //파일 업로드 ( 파일을 서버 시스템의 물리적인 경로에 저장후 UUID.jpg 리턴
        // 파일을 실제 시스템에 저장
        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation, oriImgName,
                    itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

        //상품 이미지 정보 저장 : DB에 ItemImg 테이블에 이미지 정보를 저장.
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    // 상품 이미지 데이터 수정 메서드
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception {

        //상품 이미지 수정한 경우 상품 이미지 업데이트
        if (!itemImgFile.isEmpty()) {
            // 상품 이미지 아이디를 이용해 기존에 저장한 상품 이미지 엔티티 조회
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if (!StringUtils.isEmpty(savedItemImg.getImgName())){
                fileService.deleteFile(itemImgLocation+"/"+savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            //업데이트한 상품 이미지 파일 업로드
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/"+imgName;
            //변경된 상품 이미지 정보 세팅
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }

    }


}
