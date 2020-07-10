package draylar.presstowalk.keybinds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class Keybindings {

    public static KeyBinding AUTO_WALK = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "presstowalk.walk",
            GLFW.GLFW_KEY_R,
            "presstowalk.category"
    ));

    public static KeyBinding AUTO_MINE = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "presstowalk.mine",
            GLFW.GLFW_KEY_M,
            "presstowalk.category"
    ));

    public static void init() {
        // no-op
    }
}
