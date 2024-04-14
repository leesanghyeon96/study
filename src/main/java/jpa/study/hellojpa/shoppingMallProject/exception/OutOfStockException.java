package jpa.study.hellojpa.shoppingMallProject.exception;

/*
    상품의 재고가 주문 수량보다 작을 경우 예외 정의
 */
public class OutOfStockException extends RuntimeException{

    public OutOfStockException(String message){
        super(message);
    }

}
