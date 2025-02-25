package com.example.store.mapper;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ProductMapperHelper {

    public static String mapProductsToString(List<String> products) {
        return (products != null) ? String.join(", ", products) : "";
    }

    public static List<String> mapStringToProducts(String products) {
        return (products != null && !products.isEmpty()) ? Arrays.asList(products.split(",\\s*")) : new ArrayList<>();
    }
}


