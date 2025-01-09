package androidUtils;

import org.json.JSONException;

public class JSONObject {
    org.json.JSONObject jsonObject;
    public JSONObject(){
        jsonObject = new org.json.JSONObject();
    }

    private JSONObject(org.json.JSONObject object)
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

    public JSONObject put(String name, Object value){
        try {
            return new JSONObject(jsonObject.put(name, value));
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
}
