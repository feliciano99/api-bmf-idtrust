package com.idrust.bmfpriceapi.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    public static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
