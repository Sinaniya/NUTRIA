package org.food.chain.foodchainbackend.repository;

import org.food.chain.foodchainbackend.domain.Producer;
import org.food.chain.foodchainbackend.domain.ProductTag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface ProductTagRepository extends CrudRepository<ProductTag, Long> {
    Optional<ProductTag> findByProductTagHash(String hash);

    Set<ProductTag> findAllByProductTagProducer(Producer producer);

    @Query("SELECT coalesce(max(pt.productTagId), 0) FROM ProductTag pt")
    Long getMaxId();
}
