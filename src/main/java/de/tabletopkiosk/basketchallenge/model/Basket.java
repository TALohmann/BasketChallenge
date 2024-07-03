package de.tabletopkiosk.basketchallenge.model;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Basket implements IBasket {

	@Builder.Default
	private Map<String, IProduct> inventory = new HashMap<>();

	@Builder.Default
	private String currency = "Euro";

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

	public String getFormattedTotal() {
		String pattern = "###,###.##";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		return decimalFormat.format(this.total()) + " " + currency;
	}
}
