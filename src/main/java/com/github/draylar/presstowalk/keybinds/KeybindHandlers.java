package com.github.draylar.presstowalk.keybinds;

import com.github.draylar.presstowalk.PressToWalk;
import com.github.draylar.presstowalk.mixin.KeyCodeAccessor;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class KeybindHandlers
{
    private static final MinecraftClient client = MinecraftClient.getInstance();

    private static boolean isWalking = false;
    private static boolean isMining = false;

    public static void init()
    {
        // key event
        ClientTickCallback.EVENT.register(tick ->
                {
                    handleAutoWalk();
                    handleAutoMine();
                    handleForcedResets();
                }
        );
    }

    private static void handleForcedResets()
    {
        if(client.options.keyForward.wasPressed())
        {
            isWalking = false;
        }
    }

    private static void handleAutoWalk()
    {
        if(Keybinds.autoWalk.wasPressed())
        {
            isWalking = !isWalking;

            // reset walking/sprinting if we just turned it off
            if(isWalking == false)
            {
                resetPressedKeys();
            }
        }

        if(isWalking)
        {
            // handle special mining auto-walk mechanics
            if(isMining)
            {
                // make sure they're attempting to mine
                if(client.options.keyAttack.isPressed())
                {
                    World world = client.player.world;
                    ClientPlayerEntity player = client.player;

                    // make sure we're not falling off a cliff
                    BlockPos offset = player.getBlockPos().offset(player.getHorizontalFacing()).offset(Direction.DOWN);
                    BlockState state = world.getBlockState(offset);
                    if(!state.isAir() && !(state.getBlock() == Blocks.LAVA) && !(state.getBlock() == Blocks.WATER))
                    {
                        // don't hit our head
                        if(world.getBlockState(player.getBlockPos().offset(Direction.UP).offset(player.getHorizontalFacing(), 2)).isAir())
                        {
                            KeyBinding.setKeyPressed(getConfiguredKeyCode(client.options.keyForward), true);
                        }

                        else resetPressedKeys();
                    }

                    else resetPressedKeys();

                }

                else resetPressedKeys();
            }

            else
            {
                KeyBinding.setKeyPressed(getConfiguredKeyCode(client.options.keyForward), true);

                // force sprint if the config option is toggled on
                if (PressToWalk.config.forceSprint)
                {
                    KeyBinding.setKeyPressed(getConfiguredKeyCode(client.options.keySprint), true);
                }
            }
        }
    }

    private static void handleAutoMine()
    {
        if(Keybinds.autoMine.wasPressed())
        {
            isMining = !isMining;

            if(isMining)
            {
                client.player.addChatMessage(new TranslatableComponent("presstowalk.mining.on"), false);
            }

            else
            {
                client.player.addChatMessage(new TranslatableComponent("presstowalk.mining.off"), false);
            }
        }
    }

    private static void resetPressedKeys()
    {
        KeyBinding.setKeyPressed(getConfiguredKeyCode(client.options.keyForward), false);
        KeyBinding.setKeyPressed(getConfiguredKeyCode(client.options.keySprint), false);
    }

    /**
     * Returns the configured KeyCode attached to a KeyBinding.
     * @param keyBinding
     * @return
     */
    private static InputUtil.KeyCode getConfiguredKeyCode(KeyBinding keyBinding)
    {
        return((KeyCodeAccessor) keyBinding).getConfiguredKeyCode();
    }
}
