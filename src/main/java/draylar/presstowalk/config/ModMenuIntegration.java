package draylar.presstowalk.config;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    @Environment(EnvType.CLIENT)
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screenFactory -> AutoConfig.getConfigScreen(PressToWalkConfig.class, screenFactory).get();
    }
}
