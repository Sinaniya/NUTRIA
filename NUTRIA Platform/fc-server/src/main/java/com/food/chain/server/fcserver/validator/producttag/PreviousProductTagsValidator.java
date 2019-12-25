package com.food.chain.server.fcserver.validator.producttag;

import com.food.chain.server.fcserver.constant.FCConst;
import com.food.chain.server.fcserver.domain.ProductTag;
import com.food.chain.server.fcserver.ethereum.ETHClient;
import com.food.chain.server.fcserver.repository.ProductTagRepository;
import com.food.chain.server.fcserver.wsdto.CreateProductTagWsDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Optional;

@Slf4j
public class PreviousProductTagsValidator {

    public void validatePreviousProductTags(CreateProductTagWsDTO productTag,
                                            ETHClient ethClient,
                                            ProductTagRepository productTagRepository,
                                            Map<String, String> errors) {

        // iterating over pt hashes, as soon as we encounter an error, we return
        // since it doesn't make sense to go further
        productTag.getPreviousProductTagHashes().forEach(previousProductTagHash -> {
            try {
                if (!ethClient.getFoodChainContract().isHashValid(previousProductTagHash).send()) {
                    errors.put(FCConst.PREVIOUS_PRODUCT_TAG_HASH, "there is no previous product tag on the Ethereum network for the given hash");
                    return;
                } else {
                    Optional<ProductTag> previousProductTag = productTagRepository.findByProductTagHash(previousProductTagHash);
                    if (!previousProductTag.isPresent()) {
                        errors.put(FCConst.PREVIOUS_PRODUCT_TAG_HASH, "there is no previous product tag in the database for the given hash. " +
                                "This is very bad because the data is not consistent");
                        return;
                    }
                }
            } catch (Exception e) {
                errors.put(FCConst.PREVIOUS_PRODUCT_TAG_HASH, "Error while checking if the given hash is on the eth network.");
                log.error("Error while checking if the given hash is on the eth network.");
                return;
            }
        });

    }
}