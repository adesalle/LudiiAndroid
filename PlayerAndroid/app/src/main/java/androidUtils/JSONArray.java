package androidUtils;

import androidx.annotation.NonNull;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class JSONArray implements Iterable<Object>{

    org.json.JSONArray array;

    public JSONArray(org.json.JSONArray array)
    {
        this.array = array;
    }

    public JSONArray (List<Object> list){
        try {
            array = new org.json.JSONArray(list.toArray());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public int length()
    {
        return array.length();
    }

    public List<Object> toList() {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.opt(i));
        }
        return list;
    }

    @NonNull
    @Override
    public Iterator<Object> iterator() {
        return new Iterator<Object>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < array.length();
            }

            @Override
            public Object next() {
                try {
                    return array.get(index++);
                } catch (JSONException e) {
                    throw new RuntimeException("Erreur lors de l'accès à l'élément du JSONArray", e);
                }
            }
        };
    }
}
