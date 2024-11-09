package androidUtils;

import java.util.ArrayList;
import java.util.List;

public class JSONArray {

    org.json.JSONArray array;

    public JSONArray(org.json.JSONArray array)
    {
        this.array = array;
    }

    public JSONArray (List<Object> list) {
        array = new org.json.JSONArray(list);
    }

    public List<Object> toList()
    {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.opt(i));
        }
        return list;
    }
}
