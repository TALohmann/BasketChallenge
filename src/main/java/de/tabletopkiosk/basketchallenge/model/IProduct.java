package de.tabletopkiosk.basketchallenge.model;

public interface IProduct {

	String getBarcode();

	String getName();

	int getQuantity();

	double getPrice();

	double getDiscount();

	void addQuantity(int additionalQuantity);

	void removeQuantity(int quantityToRemove);

	boolean isEmpty();

	void setDiscount(double discount);

}
