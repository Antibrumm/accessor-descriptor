package ch.mfrey.bean.ad;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class AccessorDescriptorBuilder.
 *
 * @author Martin Frey
 */
public class AccessorDescriptorBuilder {

    /**
     * Builder.
     *
     * @param type
     *            the type
     * @param liseners
     * @return the accessor descriptor builder
     */
    public static AccessorDescriptorBuilder builder(final Class<?> type,
            final List<AccessorDescriptorBuilderListener> liseners) {
        return new AccessorDescriptorBuilder(type, liseners);
    }

    protected AccessorContext accessorContext;

    private final List<AccessorDescriptorBuilderListener> listeners = new ArrayList<>();

    /** The property descriptors. */
    protected List<PropertyDescriptor> propertyDescriptors = new ArrayList<>();

    /** The type. */
    protected final Class<?> type;

    protected String fullPropertyAccessor;

    protected String propertyAccessor;

    /**
     * Instantiates a new accessor descriptor builder.
     *
     * @param type
     *            the type
     */
    protected AccessorDescriptorBuilder(final Class<?> type, final List<AccessorDescriptorBuilderListener> listeners) {
        this.type = type;
        if (listeners != null) {
            this.listeners.addAll(listeners);
        }
    }

    /**
     * Builds the.
     *
     * @return the accessor descriptor
     */
    public AccessorDescriptor build() {
        AccessorDescriptorBuilder builder = this;
        for (AccessorDescriptorBuilderListener listener : listeners) {
            builder = listener.onBuild(builder);
        }
        return new AccessorDescriptor(builder);
    }

    /**
     * Copy.
     *
     * @return the accessor descriptor builder
     */
    protected AccessorDescriptorBuilder copy() {
        AccessorDescriptorBuilder copy = new AccessorDescriptorBuilder(this.type, this.listeners);
        copy.fullPropertyAccessor = this.fullPropertyAccessor;
        copy.propertyAccessor = this.propertyAccessor;
        copy.propertyDescriptors = new ArrayList<>();
        copy.propertyDescriptors.addAll(this.propertyDescriptors);
        copy.accessorContext = new AccessorContext(this.accessorContext);
        return copy;
    }

    public AccessorContext getAccessorContext() {
        return accessorContext;
    }

    public String getPropertyAccessor() {
        return fullPropertyAccessor;
    }

    public int getPropertyLevel() {
        return this.propertyDescriptors.size() - 1;
    }

    /**
     * With property descriptor.
     *
     * @param propertyDescriptor
     *            the property descriptor
     * @return the accessor descriptor builder
     */
    public AccessorDescriptorBuilder withPropertyDescriptor(final String name,
            final PropertyDescriptor propertyDescriptor) {
        AccessorDescriptorBuilder copy = copy();
        copy.fullPropertyAccessor = copy.fullPropertyAccessor == null ? name : copy.fullPropertyAccessor + "." + name;
        copy.propertyAccessor = copy.fullPropertyAccessor.replaceAll("(\\[.*\\])?", "");
        copy.propertyDescriptors.add(propertyDescriptor);
        return copy;
    }

    public interface AccessorDescriptorBuilderListener {
        AccessorDescriptorBuilder onBuild(AccessorDescriptorBuilder builder);

    }
}
