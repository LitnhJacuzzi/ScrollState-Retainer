package org.litnhjacuzzi.scrollstateretainer.mixin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.litnhjacuzzi.scrollstateretainer.feature.ChatHudScrollData;
import org.litnhjacuzzi.scrollstateretainer.feature.ElementScrollData;
import org.litnhjacuzzi.scrollstateretainer.feature.EntryListWidgetScrollData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.EntryListWidget;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
	@Shadow
	public Screen currentScreen;
	
	@Shadow
	public InGameHud inGameHud;
	
	private boolean shouldResumeScrollState = false;
	private List<ElementScrollData<?>> scrollableElements = Collections.emptyList();
	
	@Inject(method = "setScreen", at = @At("HEAD"))
	public void checkScreen(Screen screen, CallbackInfo ci) {
		shouldResumeScrollState = currentScreen instanceof ConfirmScreen;
		if(screen instanceof ConfirmScreen && currentScreen != null) {
			scrollableElements = new ArrayList<>();
			if(currentScreen instanceof ChatScreen) {
				scrollableElements.add(new ChatHudScrollData(inGameHud.getChatHud()));
			}else {
				List<Field> scrollableElementFields = new ArrayList<>();
				getScrollableElementFields(currentScreen.getClass(), scrollableElementFields);
				scrollableElementFields.forEach(scrollableElementField -> {
					try {
						scrollableElementField.setAccessible(true);
						scrollableElements.add(new EntryListWidgetScrollData(currentScreen, scrollableElementField, 
								((EntryListWidget<?>) scrollableElementField.get(currentScreen)).getScrollAmount()));
					}catch(Exception e) {
						e.printStackTrace();
					}
				});
			}
		}
	}
	
	@Inject(method = "setScreen", at = @At(value = "INVOKE", 
			target = "Lnet/minecraft/client/gui/screen/Screen;init(Lnet/minecraft/client/MinecraftClient;II)V",
			shift = At.Shift.AFTER))
	public void restoreScrollState(Screen screen, CallbackInfo ci) {
		if(shouldResumeScrollState && !scrollableElements.isEmpty()) {
			scrollableElements.forEach(scrollableElement -> {
				scrollableElement.restoreScrollAmount();
			});
		}
	}
	
	private static void getScrollableElementFields(Class<?> screenClass, List<Field> list) {
		Class<?> superClass = screenClass.getSuperclass();
		if(superClass != Screen.class) {
			getScrollableElementFields(superClass, list);
		}
		
		Field[] fields = screenClass.getDeclaredFields();
		for(Field field : fields) {
			if(isApplicable(field.getType(), EntryListWidget.class)) {
				list.add(field);
			}
		}
	}
	
	private static boolean isApplicable(Class<?> cls, Class<?> superClass) {
		while(cls != null) {
			if(cls == superClass) {
				return true;
			}else {
				cls = cls.getSuperclass();
			}
		}
		return false;
	}
}