package com.food.chain.server.fcserver.repository;

import com.food.chain.server.fcserver.domain.Producer;
import com.food.chain.server.fcserver.domain.ProductTag;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface ProductTagRepository extends CrudRepository<ProductTag, Long> {
    Optional<ProductTag> findByProductTagHash(String hash);

    Set<ProductTag> findAllByProductTagProducer(Producer producer);
}
