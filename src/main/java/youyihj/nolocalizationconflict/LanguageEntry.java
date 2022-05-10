package youyihj.nolocalizationconflict;

import java.util.HashMap;
import java.util.Map;

/**
 * @author youyihj
 */
public class LanguageEntry {
    private String defaultValue;
    private final String firstDomain;
    private final Map<String, String> conflictValues = new HashMap<>();
    private boolean conflicted;

    public LanguageEntry(String defaultValue, String firstDomain) {
        this.defaultValue = defaultValue;
        this.firstDomain = firstDomain;
    }

    public void put(String value, String domain) {
        if (firstDomain.equals(domain)) {
            defaultValue = value;
        } else {
            conflictValues.put(domain, value);
            conflicted = true;
        }
    }

    public String get() {
        if (!conflicted) {
            return defaultValue;
        } else {
            return conflictValues.getOrDefault(NoLocalizationConflict.getCallerMod(), defaultValue);
        }
    }

    public String get(String domain) {
        if (!conflicted) {
            return defaultValue;
        } else {
            return conflictValues.getOrDefault(domain, defaultValue);
        }
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
