package org.food.chain.foodchainbackend.validator.producttag;

import lombok.extern.slf4j.Slf4j;
import org.food.chain.foodchainbackend.constant.FCConst;
import org.food.chain.foodchainbackend.domain.ProductTag;
import org.food.chain.foodchainbackend.ethereum.ETHClient;
import org.food.chain.foodchainbackend.repository.ProductTagRepository;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class PreviousProductTagValidator {

    public void validatePreviousProductTag(ProductTag productTag,
                                           ETHClient ethClient,
                                           ProductTagRepository productTagRepository,
                                           Map<String, String> errors) {


        try {
            if (!ethClient.getFoodChainContract().isHashValid(productTag.getPreviousProductTagHash()).send()) {
                errors.put(FCConst.PREVIOUS_PRODUCT_TAG_HASH, "there is no previous product tag on the Ethereum network for the given hash");
            } else {
                Optional<ProductTag> previousProductTag = productTagRepository.findByProductTagHash(productTag.getPreviousProductTagHash());
                if (!previousProductTag.isPresent()) {
                    errors.put(FCConst.PREVIOUS_PRODUCT_TAG_HASH, "there is no previous product tag in the database for the given hash. " +
                            "This is very bad because the data is not consistent");
                } else {
                    productTag.setPreviousProductTag(previousProductTag.get());
                }
            }
        } catch (Exception e) {
            errors.put(FCConst.PREVIOUS_PRODUCT_TAG_HASH, "Error while checking if the given hash is on the eth network.");
            log.error("Error while checking if the given hash is on the eth network.");
        }

    }
}
