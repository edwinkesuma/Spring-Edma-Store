package com.edwinkesuma.springedmastore.common.util;

import java.text.Normalizer;
import java.util.Locale;

public final class SlugUtil {

    private SlugUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static String toSlug(String input) {
        if (input == null || input.isBlank()) {
            return "";
        }

        return Normalizer.normalize(input, Normalizer.Form.NFD)
                // remove accent characters
                .replaceAll("\\p{M}", "")
                // lowercase
                .toLowerCase(Locale.ENGLISH)
                // replace non alphanumeric with dash
                .replaceAll("[^a-z0-9]+", "-")
                // remove leading dash
                .replaceAll("^-+", "")
                // remove trailing dash
                .replaceAll("-+$", "")
                // collapse multiple dash
                .replaceAll("-{2,}", "-");
    }
}
