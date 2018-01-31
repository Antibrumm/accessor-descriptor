package ch.mfrey.bean.ad;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

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
    private final List<PropertyDescriptor> propertyDescriptors;

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
        this.propertyDescriptors = new ArrayList<>(accessorDescriptorBuilder.propertyDescriptors);
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
     * Gets the property descriptors.
     *
     * @return the property descriptors
     */
    public List<PropertyDescriptor> getPropertyDescriptors() {
        return propertyDescriptors;
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
     * Gets the result descriptor.
     *
     * @return the result descriptor
     */
    public PropertyDescriptor getResultDescriptor() {
        return propertyDescriptors.get(propertyDescriptors.size() - 1);
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
        return "AccessorDescriptor [type=" + type.getSimpleName() //$NON-NLS-1$
                + ", propertyAccessor=" + propertyAccessor //$NON-NLS-1$
                + ", fullPropertyAccessor=" + fullPropertyAccessor //$NON-NLS-1$
                + ", propertyLevel=" + propertyLevel //$NON-NLS-1$
                + ", resultType=" + getResultDescriptor().getPropertyType().getSimpleName()
                + ", descriptors=" + propertyDescriptors + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

    public String getFullPropertyAccessor() {
        return fullPropertyAccessor;
    }

}
