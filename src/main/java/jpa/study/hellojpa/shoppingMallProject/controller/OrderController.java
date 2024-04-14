package jpa.study.hellojpa.shoppingMallProject.controller;

import jakarta.validation.Valid;
import jpa.study.hellojpa.shoppingMallProject.dto.OrderDto;
import jpa.study.hellojpa.shoppingMallProject.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /*
    새로고침 없이 주문을 요청하기 위해 비동기 방식을 사용
     */
    @PostMapping("/order")
    public @ResponseBody ResponseEntity order (@RequestBody @Valid OrderDto orderDto,
                                               BindingResult bindingResult, Principal principal){
        //오류가 있는지 검사
        if(bindingResult.hasErrors()){
            StringBuilder sb =new StringBuilder();
            List<FieldError> fieldErrors =bindingResult.getFieldErrors();

            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            //에러 정보를 ResponseEntity 객체에 담아서 전달
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long orderId;

        //주문 로직
        try{
            orderId = orderService.order(orderDto, email);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<Long>(orderId, HttpStatus.OK);

    }
}
