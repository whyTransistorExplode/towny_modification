package net.fabricmc.towny_helper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.towny_helper.gui.ModGui;
import net.fabricmc.towny_helper.gui.PlayersGUI;
import net.fabricmc.towny_helper.gui.TownsGUI;
import net.fabricmc.towny_helper.gui.manager.ScreenManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import static net.fabricmc.towny_helper.MainMod.matrices;

public class ClientMod implements ClientModInitializer {

    private  static KeyBinding homeTSpawn;
    private  static KeyBinding PlayersGui;
    private  static KeyBinding TownsGui;
    private  static KeyBinding modGui;
    private static KeyBinding safeHome;
    private static KeyBinding voteButton;
    HudRenderCallback hudRenderCallback;


    @Override
    public void onInitializeClient() {
        MainMod.setCompassTexture(new Identifier("towny_helper","textures/indicator_v2_compact.png"));
        matrices = new MatrixStack();
//        renderScreen();

        homeTSpawn = KeyBindingHelper.registerKeyBinding(
                 new KeyBinding("key.townyhelper.tspawn",
                         InputUtil.Type.KEYSYM,
                         GLFW.GLFW_KEY_F6,
                         "key.category.travel"));
         PlayersGui = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.townyhelper.playersgui",
                 InputUtil.Type.KEYSYM,
                 GLFW.GLFW_KEY_F7,"key.category.opengui"));

        TownsGui = KeyBindingHelper.registerKeyBinding(
                new KeyBinding("key.townyhelper.townsgui",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_F8,
                        "key.category.opengui"));
        modGui = KeyBindingHelper.registerKeyBinding(
                new KeyBinding("key.townyhelper.modgui",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_F9,
                        "key.category.opengui"));
        safeHome = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.townyhelper.safehome",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F10,
            "key.category.opengui"));
        voteButton = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.townyhelper.votebutton",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "key.category.opengui"));


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (homeTSpawn.wasPressed()) {
                if (MinecraftClient.getInstance().player != null)
                    MinecraftClient.getInstance().player.sendChatMessage("/tc / town spawn");

            }
            while (PlayersGui.wasPressed()){
                MinecraftClient.getInstance().setScreen(new ScreenManager(PlayersGUI.getInstance()));
            }
            while (TownsGui.wasPressed()){
                MinecraftClient.getInstance().setScreen(new ScreenManager(TownsGUI.getInstance()));
            }
            while(modGui.wasPressed()){
                MinecraftClient.getInstance().setScreen(new ScreenManager(ModGui.getInstance()));
            }
            while(safeHome.wasPressed()){
                if (MinecraftClient.getInstance().player != null)
                    MinecraftClient.getInstance().player.sendChatMessage("/tc /home");
            }
            while(voteButton.wasPressed()){
                if (MinecraftClient.getInstance().player != null)
                    MinecraftClient.getInstance().player.sendChatMessage("/iron");
            }
        });

    }
    public void renderScreen(){
//        MinecraftClient.getInstance().inGameHud.setOverlayMessage(Text.of("me ULUGBEK"), true);
//        MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier("towny_helper","blue_liked_flag.png"));
//        HudRenderCallback.EVENT.addPhaseOrdering(new Identifier("towny_helper:heart.png"),new Identifier("towny_helper:broken_heart.png"));

        if (hudRenderCallback ==null) {
            hudRenderCallback = new HudRenderCallback() {
                @Override
                public void onHudRender(MatrixStack matrixStack, float tickDelta) {
//                MinecraftClient.getInstance().inGameHud.drawTexture(matrixStack,10, 10, 0, 0,800,800);
//                System.out.println("d");
//                Screen.fill(matrixStack, 10,10,1000,100,1);
//                float yaw = MinecraftClient.getInstance().player.getYaw()%360;
//                if (yaw <0) yaw -= -360;
//                System.out.println(yaw);

               //                ScreenDrawing.texturedGuiRect(matrixStack,0,0,30,30,compassTexture,90,0,0xFF_FFFFFF);
//                ScreenDrawing.texturedRect(matrixStack,0,0,90,30,compassTexture,0xFF_FFFFFF);
//                ScreenDrawing.texturedRect(matrixStack,500,0,50,50,new Identifier("towny_helper","green_flag.png"),0xFF_FFFFFF);
//                ScreenDrawing.coloredRect(matrixStack,200,10,100,100,0x00ff00);
//                ScreenDrawing.texturedRect(matrixStack, 0, 0, 100, 100);
//                Tessellator tessellator = Tessellator.getInstance();
//                BufferBuilder buffer = tessellator.getBuffer();
//                buffer.vertex(0,0,0).texture(1,1);
                }
            };
HudRenderCallback.EVENT.register(hudRenderCallback);
        }
    }
}
