package de.tabletopkiosk.basketchallenge.model;

import java.util.Map;

public interface IBasket {

	void scan(IProduct product);

	void remove(IProduct product);

	double total();

	String getFormattedTotal();

	Map<String, IProduct> getInventory();

}
