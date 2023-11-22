package youyihj.nolocalizationconflict;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author youyihj
 */
@Mod(modid = NoLocalizationConflict.MODID, name = NoLocalizationConflict.NAME, version = NoLocalizationConflict.VERSION, dependencies = NoLocalizationConflict.DEPENDENCIES)
public class NoLocalizationConflict {
    public static final String MODID = "nolocalizationconflict";
    public static final String NAME = "No Localization Conflict";
    public static final String VERSION = "1.4";
    public static final String DEPENDENCIES = "required-after:mixinbooter@[4.2,)";
    public static final Map<URL, ModContainer> pathToModMap = new HashMap<>();
    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        Loader.instance().getActiveModList().forEach(mod -> {
            try {
                URL url = mod.getSource().toURI().toURL();
                if (!pathToModMap.containsKey(url)) {
                    pathToModMap.put(url, mod);
                }
            } catch (MalformedURLException e) {
                logger.throwing(e);
            }
        });
    }

    public static String getCallerMod() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            String className = stackTraceElement.getClassName();
            Class<?> clazz;
            try {
                clazz = Class.forName(className, false, NoLocalizationConflict.class.getClassLoader());
            } catch (ClassNotFoundException e) {
                continue;
            }
            Optional<URL> url = Optional.of(clazz).map(Class::getProtectionDomain).map(ProtectionDomain::getCodeSource).map(CodeSource::getLocation);
            if (!url.isPresent()) continue;
            ModContainer mod = pathToModMap.get(url.get());
            if (mod == null || mod.getModId().equals(NoLocalizationConflict.MODID)) continue;
            return mod.getModId();
        }
        return "";
    }
}
