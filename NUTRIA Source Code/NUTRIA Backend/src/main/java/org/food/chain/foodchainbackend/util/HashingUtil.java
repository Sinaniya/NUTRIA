package org.food.chain.foodchainbackend.util;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public final class HashingUtil {

    private HashingUtil() {
    }

    public static String getProductTagHash(Long productTagId, Date date, Double longitude, Double latitude, String previousProductTagHash) {
        String input = productTagId.toString() + date.toString() + longitude.toString() + latitude.toString() + previousProductTagHash;
        return Hashing.sha256()
                .hashString(input, StandardCharsets.UTF_8)
                .toString();
    }

}
