package org.litnhjacuzzi.scrollstateretainer.feature;

public abstract class ElementScrollData<E> {
	final E element;
	final double scrollAmount;
	
	public ElementScrollData(E element, double scrollAmount) {
		this.element = element;
		this.scrollAmount = scrollAmount;
	}
	
	public abstract void restoreScrollAmount();
}
