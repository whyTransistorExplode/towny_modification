package net.fabricmc.towny_helper.mixin;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.fabricmc.towny_helper.MainMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.fabricmc.towny_helper.MainMod.getDistance;
import static net.fabricmc.towny_helper.MainMod.matrices;

@Mixin(InGameHud.class)
public class InGameHudMixin  {


	private static final int locationX = 70;
	private static final int locationY = 0;
	float  nm1, nm2;
	@Inject(at = @At("RETURN"), method = "render",cancellable = true)
	private void onRender(MatrixStack matrices, float tickDelta, CallbackInfo info) {
		drawTextureC();
	}


	public void drawTextureC(){
//		ScreenDrawing.texturedRect(matrices, 0, 0, 510, 30, MainMod.getCompassTexture(), 0, 0, 1f, 1f, 0xFF_FFFFFF);
		if (MainMod.isIsLooking()) {
			nm1 = 0.0f; nm2 = 1;
			if (MainMod.getCompassTextureUID() > 0) {
				nm2 = MainMod.getCompassTextureUID() / 17f;
				nm1 = (MainMod.getCompassTextureUID() - 1) / 17f;
			}
			ScreenDrawing.texturedRect(matrices, locationX, locationY, 25, 25, MainMod.getCompassTexture(), nm1, 0, nm2, 1f, 0xFF_FFFFFF);
			Text text = new LiteralText(getDistance() + " blocks " + (MainMod.isPlayerHidden()?" Hidden":"")).setStyle(Style.EMPTY.withExclusiveFormatting(Formatting.GRAY));

//			ScreenDrawing.drawString(MainMod.matrices,String.valueOf(MainMod.getDistance()) + "blocks", HorizontalAlignment.LEFT,
//					locationX+ 45, locationY + 10,100,0x101010);
			Screen.drawCenteredText(matrices,MinecraftClient.getInstance().textRenderer,text,locationX + 65,locationY + 10,0xFF_FFFFFF);

		}
	}


}
