package ch.mfrey.bean.ad;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.mfrey.bean.ad.AccessorDescriptorBuilder.AccessorDescriptorBuilderListener;

public class AccessorDescriptorFactory {

    private static final Logger log = LoggerFactory.getLogger(AccessorDescriptorFactory.class);

    private final List<AccessorDescriptorBuilderListener> builderListeners = new ArrayList<>();

    private final Map<Class<?>, List<AccessorDescriptor>> scanned = new HashMap<>();

    protected void buildPropertyAccessors(final AccessorDescriptorBuilder accessorDescriptorBuilder,
            final Class<?> currentClass, final List<Class<?>> processedClasses,
            final Set<AccessorDescriptor> accessorDescriptors) {
        if (!shouldBuildPropertyAccessors(processedClasses, currentClass, accessorDescriptorBuilder)) {
            return;
        }
        List<Class<?>> lProcessedClasses = new ArrayList<>(processedClasses);
        lProcessedClasses.add(currentClass);
        try {
            Set<AccessorDescriptor> currentADs = new HashSet<>();
            for (PropertyDescriptor pd : Introspector.getBeanInfo(currentClass).getPropertyDescriptors()) {
                if (shouldIgnorePropertyDescriptor(currentClass, pd)) {
                    continue;
                }
                
                Class<?> returnType = pd.getPropertyType();
                if (returnType.isArray()) {
                    AccessorDescriptorBuilder currentBuilder =
                            accessorDescriptorBuilder.withPropertyDescriptor(pd.getName() + "[]",
                                    new BeanPropertyDescriptor(currentClass, pd));
                    buildAccessorDescriptor(currentADs, currentBuilder);
                    buildPropertyAccessors(currentBuilder, returnType.getComponentType(),
                            new ArrayList<>(lProcessedClasses),
                            currentADs);
                } else if (Collection.class.isAssignableFrom(returnType)
                        && pd.getReadMethod().getGenericReturnType() instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) pd.getReadMethod().getGenericReturnType();
                    if (parameterizedType.getActualTypeArguments()[0] instanceof Class) {
                        AccessorDescriptorBuilder currentBuilder =
                                accessorDescriptorBuilder.withPropertyDescriptor(pd.getName() + "[]",
                                        new BeanPropertyDescriptor(currentClass, pd));
                        buildAccessorDescriptor(currentADs, currentBuilder);
                        buildPropertyAccessors(currentBuilder, (Class<?>) parameterizedType.getActualTypeArguments()[0],
                                new ArrayList<>(lProcessedClasses),
                                currentADs);
                    }
                } else if (Map.class.isAssignableFrom(returnType) && pd.getReadMethod() != null
                        && pd.getReadMethod().getGenericReturnType() instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) pd.getReadMethod().getGenericReturnType();
                    if (parameterizedType.getActualTypeArguments()[0] instanceof Class) {
                        AccessorDescriptorBuilder currentBuilder =
                                accessorDescriptorBuilder.withPropertyDescriptor(pd.getName() + "[KEY]",
                                        new BeanPropertyDescriptor(currentClass, pd));
                        buildAccessorDescriptor(currentADs, currentBuilder);
                        buildPropertyAccessors(currentBuilder, (Class<?>) parameterizedType.getActualTypeArguments()[0],
                                new ArrayList<>(lProcessedClasses),
                                currentADs);
                    }
                    if (parameterizedType.getActualTypeArguments()[1] instanceof Class) {
                        AccessorDescriptorBuilder currentBuilder =
                                accessorDescriptorBuilder.withPropertyDescriptor(pd.getName() + "[VALUE]",
                                        new BeanPropertyDescriptor(currentClass, pd));
                        buildAccessorDescriptor(currentADs, currentBuilder);
                        buildPropertyAccessors(currentBuilder, (Class<?>) parameterizedType.getActualTypeArguments()[1],
                                new ArrayList<>(lProcessedClasses),
                                currentADs);
                    }
                } else {
                    AccessorDescriptorBuilder currentBuilder =
                            accessorDescriptorBuilder.withPropertyDescriptor(pd.getName(),
                                    new BeanPropertyDescriptor(currentClass, pd));
                    buildAccessorDescriptor(currentADs, currentBuilder);
                    buildPropertyAccessors(currentBuilder, returnType, new ArrayList<>(lProcessedClasses),
                            currentADs);
                }
            }
            accessorDescriptors.addAll(currentADs);
        } catch (Exception e) {
            log.error("ERROR", e);
        }
    }

    private AccessorDescriptorBuilder buildAccessorDescriptor(Set<AccessorDescriptor> currentADs,
            AccessorDescriptorBuilder currentBuilder) {
        AccessorDescriptor accessorDescriptor = currentBuilder.build();
        if (currentADs.add(accessorDescriptor)) {
            log.debug("Found: {}", accessorDescriptor); //$NON-NLS-1$
        }
        return currentBuilder;
    }

    /**
     * Builds the property accessors.
     *
     * @param clazz
     *            the clazz
     * @return the list
     */
    protected List<AccessorDescriptor> buildPropertyAccessors(final Class<?> clazz) {
        Set<AccessorDescriptor> acs = new HashSet<>();
        buildPropertyAccessors(AccessorDescriptorBuilder.builder(clazz, builderListeners), clazz,
                new ArrayList<Class<?>>(), acs);
        return toSortedList(acs);
    }

    public void configureBuilderListener(final AccessorDescriptorBuilderListener listener) {
        builderListeners.add(listener);
    }

    public List<AccessorDescriptor> getAccessorDescriptors(final Class<?> clazz) {
        if (scanned.containsKey(clazz)) {
            return scanned.get(clazz);
        }
        List<AccessorDescriptor> accessorDescriptors = buildPropertyAccessors(clazz);
        scanned.put(clazz, accessorDescriptors);
        return accessorDescriptors;
    }

    protected boolean shouldBuildPropertyAccessors(final List<Class<?>> processedClasses, final Class<?> currentClass,
            final AccessorDescriptorBuilder accessorDescriptorBuilder) {
        if (currentClass == null
                || processedClasses.contains(currentClass)
                || ClassUtils.isSimpleValueType(currentClass)
                || Array.class.isAssignableFrom(currentClass)
                || Map.class.isAssignableFrom(currentClass)
                || Collection.class.isAssignableFrom(currentClass)
                || Class.class.isAssignableFrom(currentClass)) {
            return false;
        }
        return true;
    }

    private List<AccessorDescriptor> toSortedList(final Set<AccessorDescriptor> acs) {
        List<AccessorDescriptor> accessorDescriptors = new ArrayList<>(acs);
        accessorDescriptors.sort(new AccessorDescriptorComparator());
        return accessorDescriptors;
    }

    private static class AccessorDescriptorComparator implements Comparator<AccessorDescriptor> {
        @Override
        /**
         * Order by class name, property level, or each propertydescriptor name
         *
         * @param o1
         * @param o2
         * @return
         */
        public int compare(final AccessorDescriptor o1, final AccessorDescriptor o2) {
            int result = o1.getType().getSimpleName().compareTo(o2.getType().getSimpleName());
            if (result != 0) {
                return result;
            }
            return o1.getFullPropertyAccessor().compareTo(o2.getFullPropertyAccessor());
        }
    }

    /**
     * Should ignore accessor.
     *
     * @param currentClass
     *            the current class
     * @param pd
     *            the pd
     * @return true, if successful
     */
    private boolean shouldIgnorePropertyDescriptor(final Class<?> currentClass, final PropertyDescriptor pd) {
        Method readMethod = pd.getReadMethod();
        if (currentClass == null || readMethod == null || Object.class.equals(readMethod.getDeclaringClass())) {
            return true;
        }
        return false;
    }

}
