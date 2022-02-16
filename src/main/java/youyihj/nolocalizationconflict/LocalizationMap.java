package youyihj.nolocalizationconflict;

import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;

import java.util.*;

/**
 * @author youyihj
 */
public class LocalizationMap extends AbstractMap<String, String> {
    private final ILocaleExtension locale;
    private final Map<String, String> underlying = new HashMap<>();
    private final Map<String, String> keyBasedMods = new HashMap<>();
    private final Set<String> conflictKeys = new HashSet<>();
    // {key: {mod: value}}
    private final Map<String, Map<String, String>> conflictEntries = new HashMap<>();
    private Object2BooleanArrayMap<String> keyBasedEnglish = new Object2BooleanArrayMap<>();

    public LocalizationMap(ILocaleExtension locale) {
        this.locale = locale;
        keyBasedEnglish.defaultReturnValue(true);
    }

    public LocalizationMap copy() {
        LocalizationMap copy = new LocalizationMap(locale);
        copy.underlying.putAll(underlying);
        Map<String, Map<String, String>> temp = new HashMap<>();
        conflictEntries.forEach((mod, entry) -> temp.put(mod, new HashMap<>(entry)));
        copy.conflictEntries.putAll(temp);
        copy.conflictKeys.addAll(conflictKeys);
        copy.keyBasedEnglish = keyBasedEnglish;
        return copy;
    }

    @Override
    public String put(String key, String value) {
        String origin = underlying.put(key, value);
        String mod = keyBasedMods.put(key, locale.getCurrentModifyingMod());
        boolean isEnglish = locale.getLanguage().equals("en_us");
        if (origin != null && !origin.equals(value) && keyBasedEnglish.getBoolean(key) == isEnglish) {
            conflictKeys.add(key);
            Map<String, String> entry = conflictEntries.computeIfAbsent(key, it -> new HashMap<>());
            entry.put(locale.getCurrentModifyingMod(), value);
            entry.put(mod, origin);
        } else {
            keyBasedEnglish.put(key, isEnglish);
        }
        return origin;
    }

    @Override
    public void clear() {
        underlying.clear();
        conflictKeys.clear();
        conflictEntries.clear();
        keyBasedMods.clear();
    }

    @Override
    public String get(Object k) {
        if (k.getClass() != String.class) return "";
        String key = (String) k;
        if (!conflictKeys.contains(key)) {
            return underlying.get(key);
        }
        String callerMod = NoLocalizationConflict.getCallerMod();
        return Optional.of(conflictEntries).map(it -> it.get(k)).map(it -> it.get(callerMod)).orElseGet(() -> underlying.get(k));
    }

    public String getValueExplicitMod(String key, String mod) {
        if (!conflictKeys.contains(key)) {
            return underlying.get(key);
        } else {
            return Optional.of(conflictEntries).map(it -> it.get(key)).map(it -> it.get(mod)).orElseGet(() -> underlying.get(key));
        }
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return underlying.entrySet();
    }

    @Override
    public boolean containsKey(Object key) {
        return underlying.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return underlying.containsValue(value) && conflictEntries.values().stream().anyMatch(it -> it.containsValue(value));
    }

    @Override
    public int size() {
        return underlying.size();
    }

    @Override
    public Collection<String> values() {
        Set<String> values = new HashSet<>(underlying.values());
        conflictEntries.values().forEach(it -> values.addAll(it.values()));
        return values;
    }

    @Override
    public Set<String> keySet() {
        return underlying.keySet();
    }
}
