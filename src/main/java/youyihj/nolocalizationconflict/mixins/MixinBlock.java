package youyihj.nolocalizationconflict.mixins;

import net.minecraft.block.Block;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import youyihj.nolocalizationconflict.LocalizationMap;

import java.util.Map;

/**
 * @author youyihj
 */
@Mixin(Block.class)
public abstract class MixinBlock extends IForgeRegistryEntry.Impl<Block> {
    @Shadow
    public abstract String getUnlocalizedName();

    @Inject(method = "getLocalizedName", at = @At("HEAD"), cancellable = true)
    public void getNameBasedOnDomain(CallbackInfoReturnable<String> cir) {
        Map<String, String> languageList = LanguageMap.getInstance().languageList;
        String key = this.getUnlocalizedName() + ".name";
        String value;
        if (languageList instanceof LocalizationMap) {
            value = ((LocalizationMap) languageList).getValueExplicitMod(key, this.getRegistryName().getResourceDomain());
        } else {
            value = languageList.get(key);
        }
        cir.setReturnValue(value == null ? key : value);
    }
}
