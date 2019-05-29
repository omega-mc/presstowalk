package com.github.draylar.presstowalk;

import com.github.draylar.presstowalk.config.PressToWalkConfig;
import me.sargunvohra.mcmods.autoconfig1.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class PressToWalk implements ClientModInitializer
{
	private static boolean isWalking = false;
	private static FabricKeyBinding keyBinding;
	private static PressToWalkConfig config;

	@Override
	public void onInitializeClient()
	{
		config = AutoConfig.register(PressToWalkConfig.class, GsonConfigSerializer::new).getConfig();
		registerKeybindings();
	}


	private static void registerKeybindings()
	{
		// set up keybinding
		keyBinding = FabricKeyBinding.Builder.create(
				new Identifier("presstowalk", "walk"),
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_R,
				new TranslatableComponent("presstowalk.category").getText()
		).build();

		KeyBindingRegistry.INSTANCE.register(keyBinding);


		// key event
		ClientTickCallback.EVENT.register(tick ->
				{
					if(keyBinding.wasPressed())
					{
						isWalking = !isWalking;

						// reset walking/sprinting if we just turned it off
						if(isWalking == false)
						{
							KeyBinding.setKeyPressed(MinecraftClient.getInstance().options.keyForward.getDefaultKeyCode(), false);
							KeyBinding.setKeyPressed(MinecraftClient.getInstance().options.keySprint.getDefaultKeyCode(), false);
						}
					}

					if(isWalking)
					{
						KeyBinding.setKeyPressed(MinecraftClient.getInstance().options.keyForward.getDefaultKeyCode(), true);

						// force sprint if the config option is toggled on
						if(config.forceSprint)
						{
							KeyBinding.setKeyPressed(MinecraftClient.getInstance().options.keySprint.getDefaultKeyCode(), true);
						}
					}
				}
		);
	}
}