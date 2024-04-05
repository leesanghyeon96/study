package jpa.study.hellojpa.shoppingMallProject.repository;

import jpa.study.hellojpa.shoppingMallProject.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
