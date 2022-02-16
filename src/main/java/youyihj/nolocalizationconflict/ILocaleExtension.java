package youyihj.nolocalizationconflict;

import net.minecraft.client.resources.Locale;

/**
 * @author youyihj
 */
public interface ILocaleExtension {
    String getCurrentModifyingMod();

    String getLanguage();

    Locale getSelf();
}
