package youyihj.nolocalizationconflict.mixins;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.LanguageMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import youyihj.nolocalizationconflict.LocalizationMap;

import java.util.Map;

/**
 * @author youyihj
 */
@Mixin(Item.class)
public abstract class MixinItem {

    @Inject(method = "getItemStackDisplayName", at = @At(value = "HEAD"), cancellable = true)
    public void getNameBasedOnDomain(ItemStack stack, CallbackInfoReturnable<String> cir) {
        Map<String, String> languageList = LanguageMap.getInstance().languageList;
        String key = stack.getItem().getUnlocalizedNameInefficiently(stack) + ".name";
        String value;
        if (languageList instanceof LocalizationMap) {
            value = ((LocalizationMap) languageList).getValueExplicitMod(key, stack.getItem().getRegistryName().getResourceDomain());
        } else {
            value = languageList.get(key);
        }
        cir.setReturnValue(value == null ? key : value);
    }
}
