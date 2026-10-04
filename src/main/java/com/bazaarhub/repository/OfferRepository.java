package com.bazaarhub.repository;

import com.bazaarhub.entity.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findByProductIdAndActiveTrue(Long productId);

    Optional<Offer> findByProductIdAndSellerId(Long productId, Long sellerId);

    @Query("""
        SELECT o FROM Offer o
        WHERE o.product.id = :productId
        AND o.active = true
        AND o.stockQuantity > 0
        ORDER BY o.price ASC, o.seller.rating DESC
        """)
    List<Offer> findBuyBoxCandidates(@Param("productId") Long productId);
}