package ch.mfrey.bean.ad;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class BeanPropertyDescriptor {
    private final PropertyDescriptor propertyDescriptor;
    private final Field field;

    public BeanPropertyDescriptor(Class<?> beanClass, PropertyDescriptor propertyDescriptor) {
        this.propertyDescriptor = propertyDescriptor;
        this.field = findField(beanClass, propertyDescriptor.getName());
    }

    public Field getField() {
        return field;
    }

    public PropertyDescriptor getPropertyDescriptor() {
        return propertyDescriptor;
    }

    public String getName() {
        return propertyDescriptor.getName();
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return isAnnotationPresent(field, annotationClass)
                || isAnnotationPresent(propertyDescriptor.getReadMethod(), annotationClass)
                || isAnnotationPresent(propertyDescriptor.getWriteMethod(), annotationClass);
    }

    public static boolean isAnnotationPresent(AnnotatedElement annotatedElement,
            Class<? extends Annotation> annotationClass) {
        return annotatedElement != null && annotatedElement.isAnnotationPresent(annotationClass);
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        T annotation = propertyDescriptor.getReadMethod().getAnnotation(annotationClass);
        if (annotation != null) {
            return annotation;
        }

        return field != null ? field.getAnnotation(annotationClass) : null;
    }

    public boolean isTransientModifier() {
        return field != null && Modifier.isTransient(field.getModifiers());
    }

    public boolean isStaticModifier() {
        return field != null && Modifier.isStatic(field.getModifiers());
    }

    private static Field findField(Class<?> clazz, String name) {
        Class<?> searchType = clazz;
        while (Object.class != searchType && searchType != null) {
            Field[] fields = searchType.getDeclaredFields();
            for (Field field : fields) {
                if (name.equals(field.getName())) {
                    return field;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    public Class<?> getPropertyType() {
        return propertyDescriptor.getPropertyType();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BeanPropertyDescriptor [field=").append(field).append(", propertyDescriptor=")
                .append(propertyDescriptor).append("]");
        return builder.toString();
    }

}
