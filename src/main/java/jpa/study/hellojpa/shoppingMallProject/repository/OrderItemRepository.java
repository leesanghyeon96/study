package jpa.study.hellojpa.shoppingMallProject.repository;

import jpa.study.hellojpa.shoppingMallProject.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
