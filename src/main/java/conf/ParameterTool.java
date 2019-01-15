package conf;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一次性加载配置文件，后面只能读
 */
public class ParameterTool implements Serializable, Cloneable {
    private static final Logger LOG = LoggerFactory.getLogger(ParameterTool.class);
    private static final long serialVersionUID = 1L;

    protected static final String DEFAULT_UNDEFINED = "<undefined>";

    public static ParameterTool fromArgs(String[] args) {
        int argLength = args.length;
        Map<String, String> map = new HashMap(argLength);
        for (int i = 0; i < argLength; ++i) {
            String arg = args[i];
            String[] keyValue = arg.replace(" ", "").split("=");
            if (keyValue.length != 2) {
                throw new IllegalStateException("parse arg error with arg:" + arg +", like -DMode=platform, = is reuqired");
            }
            String key = keyValue[0];
            String value = keyValue[1];
            if (key.startsWith("-D")) {
                key = key.substring(2);
            } else if (key.startsWith("-")) {
                key = key.substring(1);
            }

            if (!StringUtils.isEmpty(key) && !StringUtils.isEmpty(value)) {
                map.put(key, value);
            }
        }

        return fromMap(map);
    }

    public static ParameterTool fromPropertiesFile(String path) throws IOException {
        File propertiesFile = new File(path);
        return fromPropertiesFile(propertiesFile);
    }

    public static ParameterTool fromPropertiesFile(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException("Properties file " + file.getAbsolutePath() + " does not exist");
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            return fromPropertiesFile(fis);
        }
    }

    public static ParameterTool fromPropertiesFile(InputStream inputStream) throws IOException {
        Properties props = new Properties();
        props.load(inputStream);
        return fromMap((Map) props);
    }

    public static ParameterTool fromMap(Map<String, String> map) {
        Preconditions.checkNotNull(map, "Unable to initialize from empty map");
        return new ParameterTool(map);
    }

    public static ParameterTool fromSystemProperties() {
        return fromMap((Map) System.getProperties());
    }

    protected final Map<String, String> data;
    protected transient Map<String, String> defaultData;
    protected transient Set<String> unrequestedParameters;

    private ParameterTool(Map<String, String> data) {
        this.data = Collections.unmodifiableMap(new HashMap<>(data));

        this.defaultData = new ConcurrentHashMap<>(data.size());

        this.unrequestedParameters = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>(data.size()));

        unrequestedParameters.addAll(data.keySet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParameterTool that = (ParameterTool) o;
        return Objects.equals(data, that.data) &&
                Objects.equals(defaultData, that.defaultData) &&
                Objects.equals(unrequestedParameters, that.unrequestedParameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, defaultData, unrequestedParameters);
    }


    public int getNumberOfParameters() {
        return data.size();
    }

    public String get(String key) {
        addToDefaults(key, null);
        unrequestedParameters.remove(key);
        return data.get(key);
    }

    public String getRequired(String key) {
        addToDefaults(key, null);
        String value = get(key);
        if (value == null) {
            throw new RuntimeException("No data for required key '" + key + "'");
        }
        return value;
    }

    public String get(String key, String defaultValue) {
        addToDefaults(key, defaultValue);
        String value = get(key);
        if (value == null) {
            return defaultValue;
        } else {
            return value;
        }
    }

    public boolean has(String value) {
        addToDefaults(value, null);
        unrequestedParameters.remove(value);
        return data.containsKey(value);
    }

    public int getInt(String key) {
        addToDefaults(key, null);
        String value = getRequired(key);
        return Integer.parseInt(value);
    }

    public int getInt(String key, int defaultValue) {
        addToDefaults(key, Integer.toString(defaultValue));
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }

    public long getLong(String key) {
        addToDefaults(key, null);
        String value = getRequired(key);
        return Long.parseLong(value);
    }

    public long getLong(String key, long defaultValue) {
        addToDefaults(key, Long.toString(defaultValue));
        String value = get(key);
        if (value == null) {
            return defaultValue;
        }
        return Long.parseLong(value);
    }

    public float getFloat(String key) {
        addToDefaults(key, null);
        String value = getRequired(key);
        return Float.valueOf(value);
    }

    public float getFloat(String key, float defaultValue) {
        addToDefaults(key, Float.toString(defaultValue));
        String value = get(key);
        if (value == null) {
            return defaultValue;
        } else {
            return Float.valueOf(value);
        }
    }

    public double getDouble(String key) {
        addToDefaults(key, null);
        String value = getRequired(key);
        return Double.valueOf(value);
    }

    public double getDouble(String key, double defaultValue) {
        addToDefaults(key, Double.toString(defaultValue));
        String value = get(key);
        if (value == null) {
            return defaultValue;
        } else {
            return Double.valueOf(value);
        }
    }

    public boolean getBoolean(String key) {
        addToDefaults(key, null);
        String value = getRequired(key);
        return Boolean.valueOf(value);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        addToDefaults(key, Boolean.toString(defaultValue));
        String value = get(key);
        if (value == null) {
            return defaultValue;
        } else {
            return Boolean.valueOf(value);
        }
    }

    public short getShort(String key) {
        addToDefaults(key, null);
        String value = getRequired(key);
        return Short.valueOf(value);
    }

    public short getShort(String key, short defaultValue) {
        addToDefaults(key, Short.toString(defaultValue));
        String value = get(key);
        if (value == null) {
            return defaultValue;
        } else {
            return Short.valueOf(value);
        }
    }

    public byte getByte(String key) {
        addToDefaults(key, null);
        String value = getRequired(key);
        return Byte.valueOf(value);
    }

    public byte getByte(String key, byte defaultValue) {
        addToDefaults(key, Byte.toString(defaultValue));
        String value = get(key);
        if (value == null) {
            return defaultValue;
        } else {
            return Byte.valueOf(value);
        }
    }

    protected void addToDefaults(String key, String value) {
        String currentValue = defaultData.get(key);
        if (currentValue == null) {
            if (value == null) {
                value = DEFAULT_UNDEFINED;
            }
            defaultData.put(key, value);
        } else {
            // there is already an entry for this key. Check if the value is the undefined
            if (currentValue.equals(DEFAULT_UNDEFINED) && value != null) {
                // update key with better default value
                defaultData.put(key, value);
            }
        }
    }

    public Properties getProperties() {
        Properties props = new Properties();
        props.putAll(this.data);
        return props;
    }

    public void createPropertiesFile(String pathToFile) throws IOException {
        createPropertiesFile(pathToFile, true);
    }

    public void createPropertiesFile(String pathToFile, boolean overwrite) throws IOException {
        File file = new File(pathToFile);
        if (file.exists()) {
            if (overwrite) {
                file.delete();
            } else {
                throw new RuntimeException("File " + pathToFile + " exists and overwriting is not allowed");
            }
        }
        Properties defaultProps = new Properties();
        defaultProps.putAll(this.defaultData);
        try (final OutputStream out = new FileOutputStream(file)) {
            defaultProps.store(out, "Default file created by Flink's ParameterUtil.createPropertiesFile()");
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new ParameterTool(this.data);
    }

    public ParameterTool mergeWith(ParameterTool other) {
        Map<String, String> resultData = new HashMap<>(data.size() + other.data.size());
        resultData.putAll(data);
        resultData.putAll(other.data);

        ParameterTool ret = new ParameterTool(resultData);

        final HashSet<String> requestedParametersLeft = new HashSet<>(data.keySet());
        requestedParametersLeft.removeAll(unrequestedParameters);

        final HashSet<String> requestedParametersRight = new HashSet<>(other.data.keySet());
        requestedParametersRight.removeAll(other.unrequestedParameters);

        ret.unrequestedParameters.removeAll(requestedParametersLeft);
        ret.unrequestedParameters.removeAll(requestedParametersRight);

        return ret;
    }

    @Override
    public String toString() {
        return "ParameterTool: " + data;
    }
}
