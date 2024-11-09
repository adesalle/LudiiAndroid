package androidUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class JSONTokener {

    org.json.JSONTokener tokener;

    public JSONTokener(InputStream inputStream)
    {
        try {
            tokener = createJSONTokener(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONTokener(Reader reader)
    {
        try {
            tokener = createJSONTokener(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONTokener(String s)
    {
        tokener = new org.json.JSONTokener(s);
    }

    public org.json.JSONTokener createJSONTokener(InputStream inputStream) throws IOException {
        StringBuilder jsonStringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonStringBuilder.append(line);
            }
        }
        return new org.json.JSONTokener(jsonStringBuilder.toString());
    }

    public org.json.JSONTokener createJSONTokener(Reader reader) throws IOException {
        StringBuilder jsonStringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonStringBuilder.append(line);
            }
        }
        return new org.json.JSONTokener(jsonStringBuilder.toString());
    }
}
