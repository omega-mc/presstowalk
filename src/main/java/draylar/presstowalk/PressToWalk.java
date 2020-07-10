package draylar.presstowalk;

import draylar.presstowalk.config.PressToWalkConfig;
import draylar.presstowalk.keybinds.KeybindHandlers;
import draylar.presstowalk.keybinds.Keybindings;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class PressToWalk implements ClientModInitializer {

	public static PressToWalkConfig CONFIG = AutoConfig.register(PressToWalkConfig.class, JanksonConfigSerializer::new).getConfig();;

	@Override
	public void onInitializeClient() {
		Keybindings.init();
		KeybindHandlers.init();
	}
}
