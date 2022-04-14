package net.fabricmc.towny_helper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;

import net.fabricmc.towny_helper.gui.CloseTownScreen;
import net.fabricmc.towny_helper.gui.CloseTownsLocation;
import net.fabricmc.towny_helper.gui.ModGui;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ClientMod implements ClientModInitializer {

    private  static KeyBinding binding1;
    private  static KeyBinding binding2;
    public static BlockEntityType<DemoBlockEntity> DEMO_BLOCK_ENTITY;
    @Override
    public void onInitializeClient() {

        (new Storage()).getBlacklistedTownsFromFile();

        KeyBinding binding3;

         binding1 = KeyBindingHelper.registerKeyBinding(
                 new KeyBinding("key.cratehuntmod.spook",
                         InputUtil.Type.KEYSYM,
                         GLFW.GLFW_KEY_F6,
                         "key.category.opengui"));
         binding2 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.cratehuntmod.fastBack",
                 InputUtil.Type.KEYSYM,
                 GLFW.GLFW_KEY_F8,"key.category.fastHomeTravel"));
        binding3 = KeyBindingHelper.registerKeyBinding(
                new KeyBinding("key.cratehuntmod.ModGui",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_F7,
                        "key.category.opengui"));


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (binding1.wasPressed()) {
                MinecraftClient.getInstance().openScreen(new CloseTownScreen(new CloseTownsLocation(MainMod.getCloseTowns())));
            }
            while (binding2.wasPressed()){
                if (MinecraftClient.getInstance().player != null) {
                    MinecraftClient.getInstance().player.sendChatMessage("/t spawn");
                }
            }
            while (binding3.wasPressed()){
                MinecraftClient.getInstance().openScreen(new CloseTownScreen(new ModGui()));
            }
        });

        BlockEntityRendererRegistry.INSTANCE.register(DEMO_BLOCK_ENTITY, MyBlockEntityRenderer::new);
    }

}
