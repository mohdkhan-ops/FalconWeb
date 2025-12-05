package com.liftofftech.falcon.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {
        // utility
    }

    public static <T> List<T> readList(String resourcePath, TypeReference<List<T>> reference) {
        try (InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath)) {
            if (stream == null) {
                throw new IllegalArgumentException("Unable to locate JSON resource: " + resourcePath);
            }
            return MAPPER.readValue(stream, reference);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to parse JSON resource: " + resourcePath, e);
        }
    }
}

