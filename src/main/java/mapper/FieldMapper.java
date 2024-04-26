package mapper;

import java.util.HashMap;
import java.util.Map;

public class FieldMapper {
    private static final Map<String, String> fieldToColumnMap = new HashMap<>();

    static {
        fieldToColumnMap.put("clientId", "client_id");
        fieldToColumnMap.put("firstName", "first_name");
        fieldToColumnMap.put("lastName", "last_name");
        fieldToColumnMap.put("phoneNumber", "phone_number");

        fieldToColumnMap.put("productId", "product_id");
        fieldToColumnMap.put("imageUrl", "image_url");
        fieldToColumnMap.put("createdAt", "created_at");

        fieldToColumnMap.put("orderId", "order_id");
        fieldToColumnMap.put("totalPrice", "total_price");
    }

    public static String mapFieldNameToColumnName(String fieldName) {
        return fieldToColumnMap.getOrDefault(fieldName, fieldName.toLowerCase());
    }
}
