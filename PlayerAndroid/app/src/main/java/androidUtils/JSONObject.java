package androidUtils;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JSONObject{
    org.json.JSONObject jsonObject;
    public JSONObject(){
        jsonObject = new org.json.JSONObject();
    }

    public JSONObject(org.json.JSONObject object)
    {
        jsonObject = object;
    }

    public JSONObject(JSONTokener object)
    {
        try {
            jsonObject = new org.json.JSONObject(object.tokener);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            try {
                Object value = jsonObject.get(key);

                // Conversion récursive des valeurs JSON
                if (value instanceof org.json.JSONObject) {
                    value = new JSONObject((org.json.JSONObject) value).toMap();
                } else if (value instanceof org.json.JSONArray) {
                    value = new JSONArray((org.json.JSONArray) value).toList();
                }

                map.put(key, value);
            } catch (JSONException e) {
                throw new RuntimeException("Error converting JSONObject to Map", e);
            }
        }

        return map;
    }



    public JSONObject put(String name, Object value){
        try {
            if (value instanceof JSONObject) {
                jsonObject.put(name, ((JSONObject) value).jsonObject); // Extract the inner org.json.JSONObject
            } else {
                jsonObject.put(name, value);
            }
            return this;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getJSONObject(String name)
    {
        try {
            return new JSONObject(jsonObject.getJSONObject(name));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String getString(String name)
    {
        try {
            return jsonObject.getString(name);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public Object get(String name)
    {
        try {
            return jsonObject.get(name);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean getBoolean(String name)
    {
        try {
            return jsonObject.getBoolean(name);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean has(String name)
    {
        return jsonObject.has(name);
    }

    public String toString(int indentFactor)
    {
        try {
            return jsonObject.toString(indentFactor);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public String toString()
    {
        return jsonObject.toString();
    }

    public int getInt(String name)
    {
        try {
            return jsonObject.getInt(name);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public double getDouble(String name)
    {
        try {
            return jsonObject.getDouble(name);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONArray optJSONArray(String name)
    {
        return new JSONArray(jsonObject.optJSONArray(name));
    }
    public boolean optBoolean(String name)
    {
        return jsonObject.optBoolean(name);
    }

    public boolean optBoolean(String name, boolean fallback)
    {
        return jsonObject.optBoolean(name, fallback);
    }

    public JSONArray getJSONArray(String name){
        try {
            return  new JSONArray(jsonObject.getJSONArray(name));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public String optString(String puzzleValueSelection, String name) {
        return jsonObject.optString(puzzleValueSelection, name);
    }


    public int optInt(String tabFontSize, int i) {

        return jsonObject.optInt(tabFontSize, i);
    }

    public JSONObject optJSONObject(String s) {

        return new JSONObject(jsonObject.optJSONObject(s));
    }

    public double optDouble(String s, double v) {
        return jsonObject.optDouble(s, v);
    }

    public Iterator<?> keys() {
        return jsonObject.keys();
    }
}
