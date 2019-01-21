package ch.mfrey.bean.ad;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * The Class AccessorDescriptor.
 *
 * @author Martin Frey
 */
public class AccessorDescriptor {

    private final AccessorContext accessorContext;

    /** The property accessor. */
    private final String propertyAccessor;

    /** The property descriptors. */
    private final List<BeanPropertyDescriptor> beanPropertyDescriptors;

    /** The property level. */
    private final int propertyLevel;

    /** The type. */
    private final Class<?> type;

    private final String fullPropertyAccessor;

    /**
     * Instantiates a new accessor descriptor.
     *
     * @param accessorDescriptorBuilder
     *            the accessor descriptor builder
     */
    protected AccessorDescriptor(final AccessorDescriptorBuilder accessorDescriptorBuilder) {
        this.type = accessorDescriptorBuilder.type;
        this.beanPropertyDescriptors = new ArrayList<>(accessorDescriptorBuilder.beanPropertyDescriptors);
        this.fullPropertyAccessor = accessorDescriptorBuilder.fullPropertyAccessor;
        this.propertyAccessor = accessorDescriptorBuilder.propertyAccessor;
        this.propertyLevel = accessorDescriptorBuilder.getPropertyLevel();
        this.accessorContext = accessorDescriptorBuilder.accessorContext;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        AccessorDescriptor other = (AccessorDescriptor) obj;
        return other.toString().equals(obj.toString());
    }

    public AccessorContext getAccessorContext() {
        return accessorContext;
    }

    /**
     * Gets the property accessor.
     *
     * @return the property accessor
     */
    public String getPropertyAccessor() {
        return propertyAccessor;
    }

    /**
     * Gets the property level.
     *
     * @return the property level
     */
    public int getPropertyLevel() {
        return propertyLevel;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public Class<?> getType() {
        return type;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (toString().hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "AccessorDescriptor [type=" + type.getName() //$NON-NLS-1$
                + ", propertyAccessor=" + propertyAccessor //$NON-NLS-1$
                + ", fullPropertyAccessor=" + fullPropertyAccessor //$NON-NLS-1$
                + ", propertyLevel=" + propertyLevel //$NON-NLS-1$
                + ", resultType=" + getResultDescriptor().getPropertyType().getName()
                + ", descriptors=" + beanPropertyDescriptors + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public String getFullPropertyAccessor() {
        return fullPropertyAccessor;
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        List<BeanPropertyDescriptor> bpds = getBeanPropertyDescriptors();
        for (int i = bpds.size() - 1; i >= 0; i--) {
            if (bpds.get(i).isAnnotationPresent(annotationClass)) {
                return true;
            }
        }
        return false;
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
        List<BeanPropertyDescriptor> bpds = getBeanPropertyDescriptors();
        for (int i = bpds.size() - 1; i >= 0; i--) {
            if (bpds.get(i).isAnnotationPresent(annotationClass)) {
                return bpds.get(i).getAnnotation(annotationClass);
            }
        }
        return null;
    }

    public boolean isStaticModifier() {
        List<BeanPropertyDescriptor> bpds = getBeanPropertyDescriptors();
        for (int i = bpds.size() - 1; i >= 0; i--) {
            if (bpds.get(i).isStaticModifier()) {
                return true;
            }
        }
        return false;
    }

    public boolean isTransientModifier() {
        List<BeanPropertyDescriptor> bpds = getBeanPropertyDescriptors();
        for (int i = bpds.size() - 1; i >= 0; i--) {
            if (bpds.get(i).isTransientModifier()) {
                return true;
            }
        }
        return false;
    }

    public List<BeanPropertyDescriptor> getBeanPropertyDescriptors() {
        return beanPropertyDescriptors;
    }

    /**
     * Gets the result bean property descriptor.
     *
     * @return the result descriptor
     */
    public BeanPropertyDescriptor getResultBeanPropertyDescriptor() {
        return beanPropertyDescriptors.get(beanPropertyDescriptors.size() - 1);
    }

    /**
     * Gets the result descriptor.
     *
     * @return the result descriptor
     */
    public PropertyDescriptor getResultDescriptor() {
        return getResultBeanPropertyDescriptor().getPropertyDescriptor();
    }

}
