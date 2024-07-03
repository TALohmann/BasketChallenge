package de.tabletopkiosk.basketchallenge.model;

import java.util.Map;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Entity
@Builder
@Getter
public class ChallengeDiscount implements IDiscount {

	@NotNull
	private String name;

	Map<String, IProduct> discountedProducts;

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
				int itemsToApply = product.getQuantity() / appliedQuantity;
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
	public void addDiscountedProduct(IProduct product) {
		if (!discountedProducts.containsKey(product.getBarcode())) {
			discountedProducts.put(product.getBarcode(), product);
		}
	}

	@Override
	public IDiscount nextDiscount() {
		return nextDiscount;
	}

}
