package go.pokemon.pikachu.convert;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import go.pokemon.pikachu.util.Assert;

import java.util.List;
import java.util.Map;

public class JSON {
    private String jsonStr;

    private Object jsonObj;

    public JSON(String jsonStr) {
        Assert.notNull(jsonStr);
        this.jsonStr = jsonStr;
        jsonObj = JSONObject.parse(jsonStr);
    }


    public Map toMap() {
        return (Map) jsonObj;
    }

    public <T> T toObject(Class<T> clazz) {
        return JSONObject.parseObject(jsonStr, clazz);
    }

    public <T> List<T> toList(Class<T> clazz) {
        return JSONObject.parseArray(jsonStr, clazz);
    }

    public String jsonPath(String path) {
        JSONPath jsonPath = JSONPath.compile(path);
        Object object = jsonPath.eval(jsonObj);
        if (object == null) {
            return null;
        }
        if (object instanceof List) {
            List list = (List) object;
            if (list.size() > 0) {
                return toString(list.iterator().next());
            }
        }
        return object.toString();

    }

    private String toString(Object object) {
        if (object instanceof Map) {
            return JSONObject.toJSONString(object);
        } else {
            return String.valueOf(object);
        }
    }

    public static boolean isJSON(String test) {
        try {
            JSONObject.parseObject(test);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }


    @Override
    public String toString() {
        return jsonStr;
    }
}
