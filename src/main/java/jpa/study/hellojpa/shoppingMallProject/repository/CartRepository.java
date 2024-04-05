package jpa.study.hellojpa.shoppingMallProject.repository;

import jpa.study.hellojpa.shoppingMallProject.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
