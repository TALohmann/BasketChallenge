package de.tabletopkiosk.basketchallenge.model;

import java.util.Map;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Basket implements IBasket {

	@NotNull
	private Map<String, IProduct> inventory;

	private IDiscount discountChain;

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
		double totalPrice = 0.0;
		for (IProduct currentProduct : inventory.values()) {
			double price = currentProduct.getQuantity() * currentProduct.getPrice();
			double discount = 0.0;
			if (discountChain != null) {
				discount = discountChain.processDiscount(currentProduct);
			}
			totalPrice += (price - discount);
		}
		return totalPrice;
	}

}
