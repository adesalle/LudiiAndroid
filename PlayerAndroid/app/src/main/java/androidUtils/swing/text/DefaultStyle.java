package androidUtils.swing.text;

import java.util.HashMap;
import java.util.Map;

public class DefaultStyle extends Style {

    private Map<Object, Object> attributes = new HashMap<>();

    public DefaultStyle(String name, Style parent) {
        super(name, parent);
        this.name = name;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addAttribute(Object name, Object value) {
        attributes.put(name, value);
    }


    @Override
    public Integer getForeground() {
        return 0;
    }
}