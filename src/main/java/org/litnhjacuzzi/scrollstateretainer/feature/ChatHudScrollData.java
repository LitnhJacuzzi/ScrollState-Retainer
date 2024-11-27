package org.litnhjacuzzi.scrollstateretainer.feature;

import org.litnhjacuzzi.scrollstateretainer.mixin.ChatHudMixin;

import net.minecraft.client.gui.hud.ChatHud;

public class ChatHudScrollData extends ElementScrollData<ChatHud> {

	public ChatHudScrollData(ChatHud element) {
		super(element, ((ChatHudMixin) (Object) element).getScrolledLines());
	}

	@Override
	public void restoreScrollAmount() {
		element.scroll((int) scrollAmount);
	}
}
