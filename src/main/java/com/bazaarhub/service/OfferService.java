package com.bazaarhub.service;

import com.bazaarhub.dto.offer.OfferRequest;
import com.bazaarhub.dto.offer.OfferResponse;
import com.bazaarhub.entity.Offer;
import com.bazaarhub.entity.Product;
import com.bazaarhub.entity.ProductCondition;
import com.bazaarhub.entity.Seller;
import com.bazaarhub.exception.ResourceNotFoundException;
import com.bazaarhub.repository.OfferRepository;
import com.bazaarhub.repository.ProductRepository;
import com.bazaarhub.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    public OfferService(OfferRepository offerRepository,
                        ProductRepository productRepository,
                        SellerRepository sellerRepository) {
        this.offerRepository = offerRepository;
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
    }

    public OfferResponse create(OfferRequest request) {
        if (offerRepository.findByProductIdAndSellerId(request.productId(), request.sellerId()).isPresent()) {
            throw new IllegalStateException(
                    "Seller " + request.sellerId() + " already has an offer for product " + request.productId()
                            + " — update it instead of creating a new one.");
        }

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + request.productId()));

        Seller seller = sellerRepository.findById(request.sellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found: " + request.sellerId()));

        Offer offer = new Offer();
        offer.setProduct(product);
        offer.setSeller(seller);
        offer.setPrice(request.price());
        offer.setStockQuantity(request.stockQuantity());
        offer.setCondition(request.condition() != null ? request.condition() : ProductCondition.NEW);

        Offer saved = offerRepository.save(offer);
        return toResponse(saved);
    }

    public List<OfferResponse> getByProduct(Long productId) {
        return offerRepository.findByProductIdAndActiveTrue(productId).stream()
                .map(this::toResponse)
                .toList();
    }

    public OfferResponse getBuyBoxWinner(Long productId) {
        List<Offer> candidates = offerRepository.findBuyBoxCandidates(productId);
        if (candidates.isEmpty()) {
            throw new ResourceNotFoundException("No active offers available for product: " + productId);
        }
        return toResponse(candidates.get(0));
    }

    public void delete(Long id) {
        if (!offerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Offer not found: " + id);
        }
        offerRepository.deleteById(id);
    }

    private OfferResponse toResponse(Offer offer) {
        return new OfferResponse(
                offer.getId(),
                offer.getProduct().getId(),
                offer.getProduct().getName(),
                offer.getSeller().getId(),
                offer.getSeller().getBusinessName(),
                offer.getPrice(),
                offer.getStockQuantity(),
                offer.getCondition(),
                offer.isActive()
        );
    }
}