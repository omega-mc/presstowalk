package draylar.presstowalk.keybinds;

import draylar.presstowalk.PressToWalk;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class KeybindHandlers {

    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static boolean isWalking = false;
    private static boolean isMining = false;

    public static void init() {
        ClientTickCallback.EVENT.register(tick -> {
                    handleAutoWalk();
                    handleAutoMine();
                    handleForcedResets();
                }
        );
    }

    private static void handleForcedResets() {
        if (client.options.keyBack.wasPressed()) {
            if (isWalking) {
                client.player.sendMessage(new TranslatableText("presstowalk.autowalkstopped"), true);
                KeyBinding.setKeyPressed(KeyBindingHelper.getBoundKeyOf(client.options.keyForward), false);
            }

            isWalking = false;
        }

        if (client.options.keyForward.wasPressed()) {
            if (isWalking) {
                client.player.sendMessage(new TranslatableText("presstowalk.autowalkstopped"), true);
                KeyBinding.setKeyPressed(KeyBindingHelper.getBoundKeyOf(client.options.keyForward), false);
            }

            isWalking = false;
        }
    }

    private static void handleAutoWalk() {
        if (Keybindings.AUTO_WALK.wasPressed()) {
            isWalking = !isWalking;

            if (isWalking) {
                client.player.sendMessage(new TranslatableText("presstowalk.autowalkstarted"), true);
            }

            // reset walking/sprinting if we just turned it off
            if (!isWalking) {
                resetPressedKeys();
                client.player.sendMessage(new TranslatableText("presstowalk.autowalkstopped"), true);
            }
        }

        if (isWalking) {
            // handle special mining auto-walk mechanics
            if (isMining) {
                // make sure they're attempting to mine
                if (client.options.keyAttack.isPressed()) {
                    World world = client.player.world;
                    ClientPlayerEntity player = client.player;

                    // make sure we're not falling off a cliff
                    BlockPos offset = player.getBlockPos().offset(player.getHorizontalFacing()).offset(Direction.DOWN);
                    BlockState state = world.getBlockState(offset);
                    if (!state.isAir() && !isLiquid(state)) {
                        // don't hit our head
                        if (world.getBlockState(player.getBlockPos().offset(Direction.UP).offset(player.getHorizontalFacing(), 2)).isAir()) {
                            KeyBinding.setKeyPressed(KeyBindingHelper.getBoundKeyOf(client.options.keyForward), true);
                        } else resetPressedKeys();
                    } else resetPressedKeys();

                } else resetPressedKeys();
            } else {
                KeyBinding.setKeyPressed(KeyBindingHelper.getBoundKeyOf(client.options.keyForward), true);

                // force sprint if the config option is toggled on
                if (PressToWalk.CONFIG.forceSprint) {
                    KeyBinding.setKeyPressed(KeyBindingHelper.getBoundKeyOf(client.options.keySprint), true);
                }
            }
        }
    }

    private static void handleAutoMine() {
        if (Keybindings.AUTO_MINE.wasPressed()) {
            isMining = !isMining;

            if (isMining) {
                client.player.sendMessage(new TranslatableText("presstowalk.mining.on"), true);
            } else {
                client.player.sendMessage(new TranslatableText("presstowalk.mining.off"), true);
            }
        }
    }

    private static void resetPressedKeys() {
        KeyBinding.setKeyPressed(KeyBindingHelper.getBoundKeyOf(client.options.keyForward), false);
        KeyBinding.setKeyPressed(KeyBindingHelper.getBoundKeyOf(client.options.keySprint), false);
    }

    public static boolean isLiquid(BlockState state) {
        return (state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.LAVA);
    }
}