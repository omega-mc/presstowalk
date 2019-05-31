package com.github.draylar.presstowalk;

import com.github.draylar.presstowalk.config.PressToWalkConfig;
import com.github.draylar.presstowalk.keybinds.KeybindHandlers;
import com.github.draylar.presstowalk.keybinds.Keybinds;
import me.sargunvohra.mcmods.autoconfig1.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class PressToWalk implements ClientModInitializer
{
	public static PressToWalkConfig config;

	@Override
	public void onInitializeClient()
	{
		config = AutoConfig.register(PressToWalkConfig.class, GsonConfigSerializer::new).getConfig();
		Keybinds.init();
		KeybindHandlers.init();
	}
}
