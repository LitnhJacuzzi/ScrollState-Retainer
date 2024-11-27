package org.litnhjacuzzi.scrollstateretainer.feature;

import java.lang.reflect.Field;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.EntryListWidget;

public class EntryListWidgetScrollData extends ElementScrollData<Field> {
	private final Screen screen;

	public EntryListWidgetScrollData(Screen screen, Field element, double scrollAmount) {
		super(element, scrollAmount);
		this.screen = screen;
	}
	
	@Override
	public void restoreScrollAmount() {
		try {
			((EntryListWidget<?>) element.get(screen)).setScrollAmount(scrollAmount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return screen + " " + element + " " + scrollAmount;
	}
}
