package el;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Calculator {

    public static Double calculate(String formula) throws ScriptException {
        return calculate(formula, new HashMap<String, Object>());
    }

    public static Double calculate(String formula, Map<String, Object> valueMap) throws ScriptException {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        for (Map.Entry<String, Object> entry : valueMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            engine.put(key, value);
        }
        return (Double)engine.eval(formula);
    }
}
