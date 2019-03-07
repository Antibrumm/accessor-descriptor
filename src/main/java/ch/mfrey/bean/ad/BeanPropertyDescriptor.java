package ch.mfrey.bean.ad;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class BeanPropertyDescriptor {
    private final Field field;
    private final PropertyDescriptor propertyDescriptor;

    public BeanPropertyDescriptor(final Class<?> beanClass, final PropertyDescriptor propertyDescriptor) {
        this.propertyDescriptor = propertyDescriptor;
        this.field = findField(beanClass, propertyDescriptor.getName());
    }

    private static Field findField(final Class<?> clazz, final String name) {
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

    public static boolean isAnnotationPresent(final AnnotatedElement annotatedElement,
            final Class<? extends Annotation> annotationClass) {
        return annotatedElement != null && annotatedElement.isAnnotationPresent(annotationClass);
    }

    public <T extends Annotation> T getAnnotation(final Class<T> annotationClass) {
        T annotation = propertyDescriptor.getReadMethod().getAnnotation(annotationClass);
        if (annotation != null) {
            return annotation;
        }

        return field != null ? field.getAnnotation(annotationClass) : null;
    }

    public Field getField() {
        return field;
    }

    public String getName() {
        return propertyDescriptor.getName();
    }

    public PropertyDescriptor getPropertyDescriptor() {
        return propertyDescriptor;
    }

    public Class<?> getPropertyType() {
        return propertyDescriptor.getPropertyType();
    }

    public boolean isAnnotationPresent(final Class<? extends Annotation> annotationClass) {
        return isAnnotationPresent(field, annotationClass)
                || isAnnotationPresent(propertyDescriptor.getReadMethod(), annotationClass)
                || isAnnotationPresent(propertyDescriptor.getWriteMethod(), annotationClass);
    }

    public boolean isStaticModifier() {
        return field != null && Modifier.isStatic(field.getModifiers());
    }

    public boolean isTransientModifier() {
        return field != null && Modifier.isTransient(field.getModifiers());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BeanPropertyDescriptor [field=").append(field).append(", propertyDescriptor=")
                .append(propertyDescriptor).append("]");
        return builder.toString();
    }

}
