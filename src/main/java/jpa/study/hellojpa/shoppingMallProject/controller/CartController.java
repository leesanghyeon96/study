package jpa.study.hellojpa.shoppingMallProject.controller;

import jakarta.validation.Valid;
import jpa.study.hellojpa.shoppingMallProject.dto.CartDetailDto;
import jpa.study.hellojpa.shoppingMallProject.dto.CartItemDto;
import jpa.study.hellojpa.shoppingMallProject.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping(value = "/cart")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto,
                                              BindingResult bindingResult, Principal principal){
        //바인딩시 cartItemDto에 에러가 있는지 검사
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        //현재 로그인한 회원의 이메일 저장
        String email = principal.getName();
        Long cartItemId;

        try{
            //화면에서 넘어오는 장바구니에 담을 상품 정보와 이메일을 이용해 장바구니에 상품을 담는 로직 호출
            cartItemId = cartService.addCart(cartItemDto, email);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    //장바구니 get 메서드
    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model){
        //이메일을 이용해 장바구니에 담겨있는 상품 정보를 조회
        List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());
        //정보를 뷰로 전달
        model.addAttribute("cartItems", cartDetailList);
        return "cart/cartList";
    }

    //장바구니 상품 개수 수정 매핑
    @PatchMapping(value = "/cartItem/{cartItemId}") // @PatchMapping : 수정, 업데이트
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, @RequestParam("count") int count, Principal principal){

        if(count <= 0){
            return new ResponseEntity<String>("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
        } else if(!cartService.validateCartItem(cartItemId, principal.getName())){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    //장바구니 상품 삭제 요청 매핑
    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal){

        if(!cartService.validateCartItem(cartItemId, principal.getName())){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);

        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }
}
