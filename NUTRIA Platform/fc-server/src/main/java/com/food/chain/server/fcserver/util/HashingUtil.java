package com.food.chain.server.fcserver.util;

import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Set;

public final class HashingUtil {

    private HashingUtil() {
    }

    public static String getProductTagHash(LocalDateTime dateTime, Double longitude, Double latitude, Set<String> previousProductTagHashes) {

        String previousPTHashes = "";
        previousProductTagHashes.forEach(previousPTHash -> {
            previousPTHashes.concat(previousPTHash);
        });

        // we add random string in the beginning just to secure total randomness
        String input = RandomStringUtils.randomAlphabetic(20) +
                dateTime.toString() +
                longitude.toString() +
                latitude.toString() +
                previousPTHashes;
        return Hashing.sha256()
                .hashString(input, StandardCharsets.UTF_8)
                .toString();
    }

}
