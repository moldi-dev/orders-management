package reflection;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Reflection {
    public static Map<Object, Object> retrieveProperties(Object object) {
        Map<Object, Object> objectMap = new HashMap<>();

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            try {
                objectMap.put(field.getName(), field.get(object));
            }

            catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }

            catch (IllegalArgumentException e) {
                e.printStackTrace();
                return null;
            }
        }

        return objectMap;
    }
}
