package androidUtils.swing.text;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SimpleAttributeSet implements AttributeSet {
    public static final SimpleAttributeSet EMPTY = new SimpleAttributeSet(Collections.emptyMap());

    private final Map<Object, Object> attributes;
    private Object styleSpan;

    public SimpleAttributeSet() {
        this.attributes = new HashMap<>();
    }

    public SimpleAttributeSet(Map<Object, Object> attributes) {
        this.attributes = new HashMap<>(attributes);
    }


    @Override
    public boolean containsAttribute(Object name) {
        return attributes.containsKey(name);
    }


    @Override
    public Object getAttribute(Object name) {
        return attributes.get(name);
    }

    @Override
    public int getAttributeCount() {
        return attributes.size();
    }


    public void addAttribute(Object name, Object value) {
        attributes.put(name, value);
    }

    public void setStyle(Object styleSpan) {
        this.styleSpan = styleSpan;
    }

    @Override
    public Object getAttributeName(int index) {
        if (index < 0 || index >= attributes.size()) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
        return attributes.keySet().toArray()[index];
    }

    public void removeAttribute(Object name) {
        attributes.remove(name);
    }

    public void removeAttributes(Object[] names) {
        for (Object name : names) {
            removeAttribute(name);
        }
    }

    public void setResolveParent(AttributeSet parent) {
        // Optional: Implement if you need parent resolution
    }

    public static SimpleAttributeSet addAttribute(SimpleAttributeSet set, Object name, Object value) {
        if (set == null) {
            set = new SimpleAttributeSet();
        }
        set.addAttribute(name, value);
        return set;
    }
}