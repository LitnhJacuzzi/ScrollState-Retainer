package org.litnhjacuzzi.scrollstateretainer.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.gui.hud.ChatHud;

@Mixin(ChatHud.class)
public interface ChatHudMixin {
	@Accessor("scrolledLines")
	int getScrolledLines(); 
}
