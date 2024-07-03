package de.tabletopkiosk.basketchallenge.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Entity
@Builder
@Getter
public class BasketProduct implements IProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String barcode;

	@NotNull
	private String name;

	@Builder.Default
	private int quantity = 1;

	@NotNull
	private double price;

	@Builder.Default
	private double discount = 0.0;

	@Override
	public void addQuantity(int additionalQuantity) {
		this.quantity += additionalQuantity;
	}

	@Override
	public void removeQuantity(int quantityToRemove) {
		if (this.quantity >= quantityToRemove) {
			quantity -= quantityToRemove;
		}
	}

	public boolean isEmpty() {
		if (quantity == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void setDiscount(double discount) {
		this.discount = discount;
	}

}
