package jpa.study.hellojpa.shoppingMallProject.controller;

import jpa.study.hellojpa.shoppingMallProject.dto.ItemSearchDto;
import jpa.study.hellojpa.shoppingMallProject.dto.MainItemDto;
import jpa.study.hellojpa.shoppingMallProject.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemService itemService;

    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto,@RequestParam("page") Optional<Integer> page, Model model){

    	// pageable : (현재 페이지 , 한페이지에서 출력할 갯수 )
    	// PageRequest.of( page , 6)    //
    	// PageRequest.of(page.isPresent() ? page.get() : 0, 6);
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 6);

        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 5);

        return "main";
    }

//    @GetMapping(value = "/")
//    public String mainPage(ItemSearchDto itemSearchDto, @RequestParam("page") Optional<Integer> page, Model model) {
//        // pageable: (현재 페이지, 한 페이지에서 출력할 개수)
//        // 기본적으로 page가 null이면 0을 사용
//        int currentPage = page != null ? page : 0;
//        Pageable pageable = PageRequest.of(currentPage, 6);
//
//        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);
//
//        model.addAttribute("items", items);
//        model.addAttribute("itemSearchDto", itemSearchDto);
//        model.addAttribute("maxPage", 5);
//
//        return "main";
//    }

}
