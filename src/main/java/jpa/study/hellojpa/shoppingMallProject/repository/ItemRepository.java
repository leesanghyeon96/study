package jpa.study.hellojpa.shoppingMallProject.repository;

import jpa.study.hellojpa.shoppingMallProject.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> , QuerydslPredicateExecutor<Item> {

    // 쿼리 메서드로 실행
    List<Item> findByItemNm(String itemNm); //상품명으로 데이터 조회
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail); //상품명과 상세 설명 or조건 조회
    List<Item> findByPriceLessThan(Integer price); // price 변수보다 값이 작은 데이터를 조회
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price); // price의 값을 내림차순으로 조회

    //@Query 어노테이션으로 실행
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail); // 상품 상세로 데이터 조회
    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc",
    nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

    //Querydsl로 작성 -> ItemRepositoryTest에서 작성 테스트
    //QuerydslPredicateExcecutor를 사용한 예제 -> Test에서 작성, 위에 extends 추가
}
