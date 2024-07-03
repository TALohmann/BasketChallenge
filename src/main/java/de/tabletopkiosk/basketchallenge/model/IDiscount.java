package de.tabletopkiosk.basketchallenge.model;

public interface IDiscount {
	String getName();

	int getMinimumQuantity();

	int getAppliedQuantity();

	double getDiscountFactor();

	void addDiscountedProduct(IProduct product);

	double processDiscount(IProduct product);

	IDiscount nextDiscount();

}
