package com.github.draylar.presstowalk.keybinds;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class Keybinds
{
    static FabricKeyBinding autoWalk;
    static FabricKeyBinding autoMine;

    public static void init()
    {
        initKeybinds();
        registerKeybinds();
    }

    private static void initKeybinds()
    {
        autoWalk = FabricKeyBinding.Builder.create(
                new Identifier("presstowalk", "walk"),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                new TranslatableComponent("presstowalk.category").getText()
        ).build();

        autoMine = FabricKeyBinding.Builder.create(
                new Identifier("presstowalk", "mine"),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                new TranslatableComponent("presstowalk.category").getText()
        ).build();
    }

    private static void registerKeybinds()
    {
        KeyBindingRegistry.INSTANCE.register(autoWalk);
        KeyBindingRegistry.INSTANCE.register(autoMine);
    }
}
