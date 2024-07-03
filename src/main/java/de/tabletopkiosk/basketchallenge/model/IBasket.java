package de.tabletopkiosk.basketchallenge.model;

public interface IBasket {

	void scan(IProduct product);

	void remove(IProduct product);

	double total();

}
