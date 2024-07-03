package de.tabletopkiosk.basketchallenge.model;

import java.util.Map;

public class Basket implements IBasket {

	private Map<String, IProduct> inventory;

	@Override
	public void scan(IProduct product) {
		IProduct productInInventory = inventory.get(product.getBarcode());
		if (productInInventory == null) {
			inventory.put(product.getBarcode(), product);
		} else {
			productInInventory.addQuantity(product.getQuantity());
		}

	}

	@Override
	public void remove(IProduct product) {
		IProduct productInInventory = inventory.get(product.getBarcode());
		if (productInInventory != null) {
			inventory.put(product.getBarcode(), product);
			productInInventory.removeQuantity(product.getQuantity());
			if (productInInventory.isEmpty()) {
				inventory.remove(productInInventory.getBarcode());
			}
		}

	}

	@Override
	public double total() {
		// TODO Auto-generated method stub
		return 0.0;
	}

}
