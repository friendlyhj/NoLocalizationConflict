package youyihj.nolocalizationconflict;

import net.minecraft.client.resources.Locale;

/**
 * @author youyihj
 */
public interface ILocaleExtension {
    String getCurrentModifyingMod();

    Locale getSelf();

    boolean isEnglish();
}
