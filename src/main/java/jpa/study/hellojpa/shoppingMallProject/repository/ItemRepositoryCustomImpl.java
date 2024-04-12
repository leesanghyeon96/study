package jpa.study.hellojpa.shoppingMallProject.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jpa.study.hellojpa.shoppingMallProject.constant.ItemSellStatus;
import jpa.study.hellojpa.shoppingMallProject.dto.ItemSearchDto;
import jpa.study.hellojpa.shoppingMallProject.dto.MainItemDto;
import jpa.study.hellojpa.shoppingMallProject.dto.QMainItemDto;
import jpa.study.hellojpa.shoppingMallProject.entity.Item;
import jpa.study.hellojpa.shoppingMallProject.entity.QItem;
import jpa.study.hellojpa.shoppingMallProject.entity.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    //QueryDsl사용
    //1.JPAQueryFactory 생성 : 동적으로 쿼리를 생성하기 위해
    private JPAQueryFactory queryFactory;

    //2.생성자에 매개변수에 EntityManager를 넣기
    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    //상품 판매 상태 조건이 null인 경우 null을 리턴, 아닌 경우 해당 조건 상품만 조회
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    //각 조회 시간에 따라 조회
    private BooleanExpression regDtsAfter(String searchDateType){
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    // 검색어가 상품명이나 상품 생산자에 포함되는 경우
    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    //Query DSL : 동적 쿼리를 사용 가능(where 절에서 Null이면 쿼리를 실행하지 않고,
    //쿼리가 들어오면 쿼리가 실행
    // JPQL은 쿼리의 자동완성이 안됨.
    // Query DSL은 자동완성으로 쿼리를 만들어 낼 수 있다.

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        List<Item> content = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())		//가져올 페이지  0
                .limit(pageable.getPageSize())		//가져올 레코드수 3
                .fetch();					// 쿼리를 적용하라. List<Item>

        long total = queryFactory.select(Wildcard.count).from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }


    /*
        메인페이지에서의 검색
     */
    private BooleanExpression itemNmLike(String searchQuery){
        // 검색어가 null이 아니면 검색어가 포함되는 상품을 조회하는 조건을 반환
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemNm.like("%" + searchQuery + "%");
    }

    // Query DSL : 엔티티 클래스의 QDomain 의 객체를 생성
    //		-- 동적 쿼리를 적용 할 수 있다.  null이 반환이되면 처리하지 않고, null 아니면 처리하게된다.
    // QDomain 의 엔티티 클래스를 생성 해야함.
    // 사용할 Entity 클래스 이름 앞에 QItem , QItemImg

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        List<MainItemDto> content = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)   //itemImg와 item을 내부조인
                .where(itemImg.repimgYn.eq("Y"))    //대표 상품 이미지만 불러오기
                .where(itemNmLike(itemSearchDto.getSearchQuery()))   //null 리턴되면 처리하지 않고, 그렇지 않으면 처리
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())		//몇 번째 페이지를 가지고 올것인가.(0, 1, 2)
                .limit(pageable.getPageSize())		//한 페이지에서 출력할 레코드 갯수  (6)
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        //Page인터페이스 : PageImpl 구현체
        //List , Pageable, 레코드 총갯수 ;
        return new PageImpl<>(content, pageable, total);
    }
}
