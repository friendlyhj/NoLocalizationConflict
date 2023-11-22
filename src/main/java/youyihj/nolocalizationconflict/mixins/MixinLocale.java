package youyihj.nolocalizationconflict.mixins;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.Locale;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import youyihj.nolocalizationconflict.ILocaleExtension;
import youyihj.nolocalizationconflict.LocalizationMap;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author youyihj
 */
@Mixin(Locale.class)
public abstract class MixinLocale implements ILocaleExtension {

    @Shadow
    Map<String, String> properties;

    @Unique
    private String nlc$currentModifyingMod;

    @Inject(method = "<init>", at = @At(value = "RETURN"))
    private void setProperties(CallbackInfo ci) {
        properties = new LocalizationMap(this);
    }

    @Inject(method = "loadLocaleData(Ljava/util/List;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/IResource;getInputStream()Ljava/io/InputStream;"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void setCurrentModifyingMod(List<IResource> resourcesList, CallbackInfo ci, Iterator<IResource> resourceIterator, IResource resource) {
        nlc$currentModifyingMod = resource.getResourceLocation().getResourceDomain();
    }

    @Override
    public Locale nlc$getSelf() {
        return ((Locale) ((Object) this));
    }

    @Override
    public String nlc$getCurrentModifyingMod() {
        return nlc$currentModifyingMod;
    }
}
