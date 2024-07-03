package de.tabletopkiosk.basketchallenge.model;

public interface IUser {
	
	String getEmail();
	
	String getName();
	
    IBasket getBasket();

}
