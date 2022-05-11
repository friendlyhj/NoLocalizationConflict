package youyihj.nolocalizationconflict;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author youyihj
 */
public class LocalizationMap extends AbstractMap<String, String> {
    private final ILocaleExtension locale;
    private final Map<String, LanguageEntry> localization = new HashMap<>();

    public LocalizationMap(ILocaleExtension locale) {
        this.locale = locale;
    }

    public LocalizationMap copy() {
        LocalizationMap copy = new LocalizationMap(locale);
        copy.localization.putAll(this.localization);
        return copy;
    }

    @Override
    public String put(String key, String value) {
        LanguageEntry entry = localization.get(key);
        if (entry == null) {
            localization.put(key, new LanguageEntry(value, locale.getCurrentModifyingMod()));
            return null;
        } else {
            entry.put(value, locale.getCurrentModifyingMod());
            return entry.getDefaultValue();
        }
    }

    @Override
    public void clear() {
        localization.clear();
    }

    @Override
    public String get(Object k) {
        LanguageEntry entry = localization.get(k);
        return entry == null ? null : entry.get();
    }

    public String getValueExplicitMod(String key, String mod) {
        LanguageEntry entry = localization.get(key);
        return entry == null ? null : entry.get(mod);
    }

    @Override
    public Set<Entry<String, String>> entrySet() {
        return localization.entrySet().stream()
            .collect(Collectors.toMap(Entry::getKey, entry -> entry.getValue().getDefaultValue()))
                .entrySet();
    }

    @Override
    public boolean containsKey(Object key) {
        return localization.containsKey(key);
    }

    @Override
    public int size() {
        return localization.size();
    }

    @Override
    public Collection<String> values() {
        return localization.values().stream().map(LanguageEntry::getDefaultValue).collect(Collectors.toList());
    }

    @Override
    public Set<String> keySet() {
        return localization.keySet();
    }
}
