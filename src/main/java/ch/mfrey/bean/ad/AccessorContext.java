package ch.mfrey.bean.ad;

import java.util.HashMap;
import java.util.Map;

public class AccessorContext {

    private final Map<Object, Object> attributes = new HashMap<>();

    public AccessorContext() {
        super();
    }

    public AccessorContext(final AccessorContext accessorContext) {
        super();
        if (accessorContext != null) {
            attributes.putAll(accessorContext.attributes);
        }
    }

    @SuppressWarnings("unchecked")
    public <E> E getAttribute(final Object key) {
        return (E) attributes.get(key);
    }

    public <E> AccessorContext setAttribute(final Object key, final E value) {
        attributes.put(key, value);
        return this;
    }
}
