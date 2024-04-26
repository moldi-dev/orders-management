package dao;

import connection.ConnectionFactory;
import mapper.FieldMapper;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());
    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public T findById(Long id) {
        System.out.println(createFindByIdQuery(id));

        try (PreparedStatement statement = ConnectionFactory.getConnection().prepareStatement(createFindByIdQuery(id))) {
            ResultSet resultSet = statement.executeQuery();

            return createObject(resultSet);
        }

        catch (SQLException e) {
            LOGGER.severe("Error occurred while finding entity by ID: " + e.getMessage());
        }

        return null;
    }

    public List<T> findAll() {
        System.out.println(createFindAllQuery());

        try (PreparedStatement statement = ConnectionFactory.getConnection().prepareStatement(createFindAllQuery())) {
            ResultSet resultSet = statement.executeQuery();

            return createObjects(resultSet);
        }

        catch (SQLException e) {
            LOGGER.severe("Error occurred while finding all entities: " + e.getMessage());
        }

        return null;
    }

    public T insert(T entity) {
        System.out.println(createInsertQuery(entity));

        try (PreparedStatement statement = ConnectionFactory.getConnection().prepareStatement(createInsertQuery(entity), Statement.RETURN_GENERATED_KEYS)) {
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.severe("Creating entity failed, no rows affected.");
                return null;
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    return findById(id);
                }

                else {
                    LOGGER.severe("Creating entity failed, no ID obtained.");
                    return null;
                }
            }
        }

        catch (SQLException e) {
            LOGGER.severe("Error occurred while creating entity: " + e.getMessage());
            return null;
        }
    }

    public T updateById(Long id, T updatedEntity) {
        String query = createUpdateByIdQuery(id, updatedEntity);
        System.out.println(query);

        try (PreparedStatement statement = ConnectionFactory.getConnection().prepareStatement(query)) {
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                LOGGER.severe("Updating entity by ID failed, no rows affected.");
                return null;
            }

            return updatedEntity;
        }

        catch (SQLException e) {
            LOGGER.severe("Error occurred while updating entity by ID: " + e.getMessage());
            return null;
        }
    }

    public int deleteById(Long id) {
        String query = createDeleteByIdQuery(id);
        System.out.println(query);

        try (PreparedStatement preparedStatement = ConnectionFactory.getConnection().prepareStatement(query)) {
            return preparedStatement.executeUpdate();
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private String createFindAllQuery() {
        StringBuilder query = new StringBuilder();

        query
                .append("SELECT * FROM ")
                .append(entityClass.getSimpleName().toLowerCase())
                .append("s;");

        return query.toString();
    }

    private String createFindByIdQuery(Long id) {
        StringBuilder query = new StringBuilder();

        query
                .append("SELECT * FROM ")
                .append(entityClass.getSimpleName().toLowerCase())
                .append("s WHERE ")
                .append(entityClass.getSimpleName().toLowerCase())
                .append("_id = ").append(id)
                .append(";");

        return query.toString();
    }

    private String createInsertQuery(T entity) {
        StringBuilder query = new StringBuilder();
        String entityId = entityClass.getSimpleName().toLowerCase() + "Id";

        query.append("INSERT INTO ")
                .append(entityClass.getSimpleName().toLowerCase())
                .append("s (");

        Field[] fields = entityClass.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            if (!fields[i].getName().equals(entityId) && !fields[i].getName().equals("createdAt")) {
                query.append(FieldMapper.mapFieldNameToColumnName(fields[i].getName()));

                if (i < fields.length - 1) {
                    query.append(", ");
                }
            }
        }

        query.append(") VALUES (");

        for (int i = 0; i < fields.length; i++) {
            if (!fields[i].getName().equals(entityId) && !fields[i].getName().equals("createdAt")) {
                fields[i].setAccessible(true);

                try {
                    query.append("'").append(fields[i].get(entity)).append("'");
                }

                catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }

                if (i < fields.length - 1) {
                    query.append(", ");
                }
            }
        }

        query.append(");");
        return query.toString();
    }

    public String createUpdateByIdQuery(Long id, T updatedEntity) {
        StringBuilder query = new StringBuilder();
        String entityId = entityClass.getSimpleName().toLowerCase() + "Id";

        query
                .append("UPDATE ")
                .append(entityClass.getSimpleName().toLowerCase())
                .append("s SET ");

        Field[] fields = entityClass.getDeclaredFields();

        for (int i = 0; i < fields.length; i++) {
            if (!fields[i].getName().equals(entityId) && !fields[i].getName().equals("createdAt")) {
                fields[i].setAccessible(true);

                query.append(FieldMapper.mapFieldNameToColumnName(fields[i].getName()));
                query.append(" = '");

                try {
                    query.append(fields[i].get(updatedEntity)).append("'");
                }

                catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }

                if (i < fields.length - 1) {
                    query.append(", ");
                }
            }
        }

        query.append(" WHERE ");
        query.append(entityClass.getSimpleName().toLowerCase()).append("_id = ").append(id).append(";");

        return query.toString();
    }

    private String createDeleteByIdQuery(Long id) {
        StringBuilder query = new StringBuilder();

        query
                .append("DELETE FROM ")
                .append(entityClass.getSimpleName().toLowerCase())
                .append("s WHERE ")
                .append(entityClass.getSimpleName().toLowerCase())
                .append("_id = ")
                .append(id)
                .append(";");

        return query.toString();
    }

    private T createObject(ResultSet resultSet) {
        T instance = null;

        Constructor[] constructors = entityClass.getDeclaredConstructors();
        Constructor constructor = null;

        for (int i = 0; i < constructors.length; i++) {
            constructor = constructors[i];

            if (constructor.getGenericParameterTypes().length == 0) {
                break;
            }
        }

        try {
            if (resultSet.next()) {
                constructor.setAccessible(true);
                instance = (T)constructor.newInstance();

                for (Field field : entityClass.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(FieldMapper.mapFieldNameToColumnName(fieldName));
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, entityClass);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
            }
        }

        catch (InstantiationException e) {
            e.printStackTrace();
        }

        catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        catch (SecurityException e) {
            e.printStackTrace();
        }

        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        catch (IntrospectionException e) {
            e.printStackTrace();
        }

        return instance;
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<>();

        Constructor[] constructors = entityClass.getDeclaredConstructors();
        Constructor constructor = null;

        for (int i = 0; i < constructors.length; i++) {
            constructor = constructors[i];

            if (constructor.getGenericParameterTypes().length == 0) {
                break;
            }
        }

        try {
            while (resultSet.next()) {
                constructor.setAccessible(true);
                T instance = (T)constructor.newInstance();

                for (Field field : entityClass.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(FieldMapper.mapFieldNameToColumnName(fieldName));
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, entityClass);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }

                list.add(instance);
            }
        }

        catch (InstantiationException e) {
            e.printStackTrace();
        }

        catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        catch (SecurityException e) {
            e.printStackTrace();
        }

        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        catch (IntrospectionException e) {
            e.printStackTrace();
        }

        return list;
    }
}
