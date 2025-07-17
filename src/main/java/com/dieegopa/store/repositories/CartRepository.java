package com.dieegopa.store.repositories;

import com.dieegopa.store.entities.Cart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {

    @EntityGraph(attributePaths = {"items.product"})
    @Query("SELECT c FROM Cart c WHERE c.id = :cartId")
    Optional<Cart> getCartWithItems(@Param("cartId") String cartId);
}