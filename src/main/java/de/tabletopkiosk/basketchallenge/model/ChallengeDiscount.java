package de.tabletopkiosk.basketchallenge.model;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChallengeDiscount implements IDiscount {

	@NotNull
	private String name;

	@Builder.Default
	Map<String, String> discountedProducts = new HashMap<>();

	@Builder.Default
	private int minimumQuantity = 1;

	@Builder.Default
	private int appliedQuantity = 1;

	@NotNull
	private double discountFactor;

	private IDiscount nextDiscount;

	public double processDiscount(IProduct product) {
		if (discountedProducts.containsKey(product.getBarcode())) {
			if (product.getQuantity() >= minimumQuantity) {
				int itemsToApply = product.getQuantity() - (product.getQuantity() % appliedQuantity);
				return itemsToApply * product.getPrice() * discountFactor;
			}
		}
		if (nextDiscount != null) {
			return nextDiscount.processDiscount(product);
		} else {
			return 0.0;
		}
	}

	@Override
	public void addDiscountedProduct(String barcode) {
		if (!discountedProducts.containsKey(barcode)) {
			discountedProducts.put(barcode, barcode);
		}
	}

	@Override
	public IDiscount nextDiscount() {
		return nextDiscount;
	}

}
