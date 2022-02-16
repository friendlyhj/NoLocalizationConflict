package youyihj.nolocalizationconflict.mixins;

import net.minecraft.util.text.translation.LanguageMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import youyihj.nolocalizationconflict.LocalizationMap;

import java.util.Map;

/**
 * @author youyihj
 */
@Mixin(LanguageMap.class)
public abstract class MixinLanguageMap {

    @Inject(method = "replaceWith", at = @At("HEAD"), cancellable = true)
    private static void setList(Map<String, String> map, CallbackInfo ci) {
        if (map.getClass() == LocalizationMap.class) {
            LanguageMap.getInstance().languageList = ((LocalizationMap) map).copy();
        }
        LanguageMap.getInstance().lastUpdateTimeInMilliseconds = System.currentTimeMillis();
        ci.cancel();
    }
}
