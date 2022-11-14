package net.fabricmc.towny_helper.mixin;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

import static net.fabricmc.towny_helper.MainMod.getDistance;
import static net.fabricmc.towny_helper.MainMod.matrices;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    private static final int locationX = 70;
    private static final int locationY = 0;
    private long millisStartTime = 0;
    private final static int LOCAL_STORAGE_UPDATE_TIME = 10000; // 10 seconds
	private boolean writeOperation = false;
    float nm1, nm2;

    @Inject(at = @At("RETURN"), method = "render")
    private void onRender(MatrixStack matrices, float tickDelta, CallbackInfo info) {
        drawTextureC();
    }


    public void drawTextureC() {
//		ScreenDrawing.texturedRect(matrices, 0, 0, 510, 30, MainMod.getCompassTexture(), 0, 0, 1f, 1f, 0xFF_FFFFFF);
        if (MainMod.isIsLooking()) {
            nm1 = 0.0f;
            nm2 = 1;
            if (MainMod.getCompassTextureUID() > 0) {
                nm2 = MainMod.getCompassTextureUID() / 17f;
                nm1 = (MainMod.getCompassTextureUID() - 1) / 17f;
            }
            ScreenDrawing.texturedRect(matrices, locationX, locationY, 25, 25, MainMod.getCompassTexture(), nm1, 0, nm2, 1f, 0xFF_FFFFFF);
            Text text = new LiteralText(getDistance() + " blocks " + (MainMod.isPlayerHidden() ? " Hidden" : "")).setStyle(Style.EMPTY.withExclusiveFormatting(Formatting.GRAY));

/*
			ScreenDrawing.drawString(MainMod.matrices,String.valueOf(MainMod.getDistance()) + "blocks", HorizontalAlignment.LEFT,
					locationX+ 45, locationY + 10,100,0x101010);
*/
            Screen.drawCenteredText(matrices, MinecraftClient.getInstance().textRenderer, text, locationX + 65, locationY + 10, 0xFF_FFFFFF);

        }
    }


    @Inject(at = @At("TAIL"), method = "tick")
    public void timing(CallbackInfo callbackInfo) {
        if (System.currentTimeMillis() - millisStartTime > LOCAL_STORAGE_UPDATE_TIME) {
            millisStartTime = System.currentTimeMillis();
            new Thread(() -> {
				if (!writeOperation){
                if (Storage.getInstance().blackedTownsStateChange) { // this is for blacked towns
						writeOperation = true;
						Storage.getInstance().blackedTownsStateChange = false;
						try {
							Storage.getInstance().threadListWriteGSON(Storage.getInstance().getBlackedTowns(), Storage.getInstance().filePathCombiner(MainMod.getServerName(), Storage.BLACKED_TOWNS_SUFFIX));
						} catch (IOException e) {
							e.printStackTrace();
						}
						writeOperation = false;
                }
				if (Storage.getInstance().whiteTownStateChange){ // this is for white towns
					writeOperation = true;
					Storage.getInstance().whiteTownStateChange = false;

					try {
						Storage.getInstance().threadListWriteGSON(Storage.getInstance().getWhiteTowns(), Storage.getInstance().filePathCombiner(MainMod.getServerName(), Storage.WHITE_TOWNS_SUFFIX));
					} catch (IOException e) {
						e.printStackTrace();
					}
					writeOperation = false;
				}
				}
            }).start();
        }
    }

}
