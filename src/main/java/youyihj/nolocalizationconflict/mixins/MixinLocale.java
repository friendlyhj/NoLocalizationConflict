package youyihj.nolocalizationconflict.mixins;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.Locale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import youyihj.nolocalizationconflict.ILocaleExtension;
import youyihj.nolocalizationconflict.LocalizationMap;

import java.io.InputStream;
import java.util.Map;

/**
 * @author youyihj
 */
@Mixin(Locale.class)
public abstract class MixinLocale implements ILocaleExtension {
    @Shadow
    Map<String, String> properties;

    private String currentModifyingMod;

    private String language;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void setProperties(CallbackInfo ci) {
        properties = new LocalizationMap(this);
    }

    @Redirect(method = "loadLocaleDataFiles", at = @At(value = "INVOKE", target = "Ljava/lang/String;format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;"))
    private String setLanguage(String s, Object[] objects) {
        language = ((String) objects[0]);
        return String.format(s, objects);
    }

    @Redirect(method = "loadLocaleData(Ljava/util/List;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/IResource;getInputStream()Ljava/io/InputStream;"))
    private InputStream setCurrentModifyingMod(IResource instance) {
        currentModifyingMod = instance.getResourceLocation().getResourceDomain();
        return instance.getInputStream();
    }

    @Override
    public Locale getSelf() {
        return ((Locale) ((Object) this));
    }

    @Override
    public String getCurrentModifyingMod() {
        return currentModifyingMod;
    }

    @Override
    public String getLanguage() {
        return language;
    }
}
