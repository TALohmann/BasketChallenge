package de.tabletopkiosk.basketchallenge;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import de.tabletopkiosk.basketchallenge.model.Basket;
import de.tabletopkiosk.basketchallenge.model.BasketProduct;
import de.tabletopkiosk.basketchallenge.model.ChallengeDiscount;
import de.tabletopkiosk.basketchallenge.model.IBasket;
import de.tabletopkiosk.basketchallenge.model.IDiscount;
import de.tabletopkiosk.basketchallenge.model.IProduct;

@SpringBootTest
public class TestBasket {

	@Test
	public void whenBasketScanProduct() {
		// given
		IBasket testBasket = Basket.builder().build();

		IProduct product1 = BasketProduct.builder().barcode("A0001").name("Product 1").quantity(1).price(12.99).build();
		IProduct product2 = BasketProduct.builder().barcode("A0002").name("Product 2").quantity(1).price(3.99).build();
		IProduct product3 = BasketProduct.builder().barcode("A0003").name("Product 3").quantity(5).price(0.99).build();
		IProduct product4 = BasketProduct.builder().barcode("A0004").name("Product 4").quantity(2).price(4.50).build();

		// when
		testBasket.scan(product1);
		testBasket.scan(product2);
		testBasket.scan(product3);
		testBasket.scan(product4);
		testBasket.scan(product2);

		// then
		assertThat(testBasket.getInventory().size()).isEqualTo(4);
		assertThat(testBasket.getInventory().get(product2.getBarcode())).isNotNull();
		assertThat(testBasket.getInventory().get(product2.getBarcode()).getQuantity()).isEqualTo(2);
		assertThat(testBasket.getInventory().get(product3.getBarcode())).isNotNull();
		assertThat(testBasket.getInventory().get(product3.getBarcode()).getQuantity()).isEqualTo(5);
		assertThat(testBasket.getInventory().get(product4.getBarcode())).isNotNull();
		assertThat(testBasket.getInventory().get(product4.getBarcode()).getPrice()).isEqualTo(4.50);
	}

	@Test
	public void whenBasketRemoveProduct() {
		// given
		IProduct product1 = BasketProduct.builder().barcode("A0001").name("Product 1").quantity(1).price(12.99).build();
		IProduct product2 = BasketProduct.builder().barcode("A0002").name("Product 2").quantity(2).price(3.99).build();
		IProduct product3 = BasketProduct.builder().barcode("A0003").name("Product 3").quantity(5).price(0.99).build();
		IProduct product4 = BasketProduct.builder().barcode("A0004").name("Product 4").quantity(2).price(4.50).build();

		Map<String, IProduct> testInventory = new HashMap<>();
		testInventory.put(product1.getBarcode(), product1);
		testInventory.put(product2.getBarcode(), product2);
		testInventory.put(product3.getBarcode(), product3);
		testInventory.put(product4.getBarcode(), product4);

		IBasket testBasket = Basket.builder().inventory(testInventory).build();

		IProduct removeProduct1 = BasketProduct.builder().barcode("A0001").name("Product 1").quantity(1).price(12.99)
				.build();
		IProduct removeProduct2 = BasketProduct.builder().barcode("A0002").name("Product 2").quantity(1).price(3.99)
				.build();
		IProduct removeProduct3 = BasketProduct.builder().barcode("A0003").name("Product 3").quantity(3).price(0.99)
				.build();

		// when
		testBasket.remove(removeProduct1);
		testBasket.remove(removeProduct2);
		testBasket.remove(removeProduct3);

		// then
		assertThat(testBasket.getInventory().size()).isEqualTo(3);
		assertThat(testBasket.getInventory().get(product1.getBarcode())).isNull();
		assertThat(testBasket.getInventory().get(product2.getBarcode())).isNotNull();
		assertThat(testBasket.getInventory().get(product2.getBarcode()).getQuantity()).isEqualTo(1);
		assertThat(testBasket.getInventory().get(product3.getBarcode())).isNotNull();
		assertThat(testBasket.getInventory().get(product3.getBarcode()).getQuantity()).isEqualTo(2);
		assertThat(testBasket.getInventory().get(product4.getBarcode())).isNotNull();
		assertThat(testBasket.getInventory().get(product4.getBarcode()).getQuantity()).isEqualTo(2);
	}

	@Test
	public void whenBasketTotal() {
		// given
		IProduct product1 = BasketProduct.builder().barcode("A0001").name("Product 1").quantity(1).price(12.99).build();
		IProduct product2 = BasketProduct.builder().barcode("A0002").name("Product 2").quantity(2).price(3.99).build();
		IProduct product3 = BasketProduct.builder().barcode("A0003").name("Product 3").quantity(5).price(0.99).build();
		IProduct product4 = BasketProduct.builder().barcode("A0004").name("Product 4").quantity(2).price(4.50).build();
		double priceOfAllProducts = product1.getQuantity() * product1.getPrice()
				+ product2.getQuantity() * product2.getPrice() + product3.getQuantity() * product3.getPrice()
				+ product4.getQuantity() * product4.getPrice();
		String pattern = "###,###.##";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		String priceOfAllProductsString = decimalFormat.format(priceOfAllProducts) + " Euro";

		Map<String, IProduct> testInventory = new HashMap<>();
		testInventory.put(product1.getBarcode(), product1);
		testInventory.put(product2.getBarcode(), product2);
		testInventory.put(product3.getBarcode(), product3);
		testInventory.put(product4.getBarcode(), product4);

		IBasket testBasket = Basket.builder().inventory(testInventory).build();

		// when
		double totalPrice = testBasket.total();
		String totalPriceFormatted = testBasket.getFormattedTotal();

		// then
		assertThat(testBasket.getInventory().size()).isEqualTo(4);
		assertThat(totalPrice).isEqualTo(priceOfAllProducts);
		assertThat(totalPriceFormatted).isEqualTo(priceOfAllProductsString);
	}

	@Test
	public void whenBasketTotalWithDiscountBuy1Get1Free() {
		// given
		IProduct product1 = BasketProduct.builder().barcode("A0001").name("Product 1").quantity(1).price(12.99).build();
		IProduct product2 = BasketProduct.builder().barcode("A0002").name("Product 2").quantity(2).price(3.99).build();
		IProduct product3 = BasketProduct.builder().barcode("A0003").name("Product 3").quantity(5).price(0.99).build();

		IDiscount discountBuy1Get1Free = ChallengeDiscount.builder().minimumQuantity(2).appliedQuantity(2)
				.discountFactor(0.5).build();

		discountBuy1Get1Free.addDiscountedProduct(product2.getBarcode());
		discountBuy1Get1Free.addDiscountedProduct(product3.getBarcode());

		double priceOfAllProducts = product1.getQuantity() * product1.getPrice()
				+ product2.getQuantity() * product2.getPrice() * 0.5 + 4 * product3.getPrice() * 0.5
				+ 1 * product3.getPrice();

		Map<String, IProduct> testInventory = new HashMap<>();
		testInventory.put(product1.getBarcode(), product1);
		testInventory.put(product2.getBarcode(), product2);
		testInventory.put(product3.getBarcode(), product3);

		IBasket testBasket = Basket.builder().inventory(testInventory).discountChain(discountBuy1Get1Free).build();

		// when
		double totalPrice = testBasket.total();

		// then
		assertThat(testBasket.getInventory().size()).isEqualTo(3);
		assertThat(totalPrice).isEqualTo(priceOfAllProducts);
	}

	@Test
	public void whenBasketTotalWithDiscount10Percent() {
		// given
		IProduct product1 = BasketProduct.builder().barcode("A0001").name("Product 1").quantity(1).price(12.99).build();
		IProduct product2 = BasketProduct.builder().barcode("A0002").name("Product 2").quantity(2).price(3.99).build();
		IProduct product3 = BasketProduct.builder().barcode("A0003").name("Product 3").quantity(5).price(0.99).build();

		IDiscount discount10Percent = ChallengeDiscount.builder().minimumQuantity(1).appliedQuantity(1)
				.discountFactor(0.1).build();

		discount10Percent.addDiscountedProduct(product1.getBarcode());

		double priceOfAllProducts = product1.getQuantity() * product1.getPrice() * 0.9
				+ product2.getQuantity() * product2.getPrice() + product3.getQuantity() * product3.getPrice();

		Map<String, IProduct> testInventory = new HashMap<>();
		testInventory.put(product1.getBarcode(), product1);
		testInventory.put(product2.getBarcode(), product2);
		testInventory.put(product3.getBarcode(), product3);

		IBasket testBasket = Basket.builder().inventory(testInventory).discountChain(discount10Percent).build();

		// when
		double totalPrice = testBasket.total();

		// then
		assertThat(testBasket.getInventory().size()).isEqualTo(3);
		assertThat(totalPrice).isEqualTo(priceOfAllProducts);
	}

	@Test
	public void whenBasketTotalWithBothDiscounts() {
		// given
		IProduct product1 = BasketProduct.builder().barcode("A0001").name("Product 1").quantity(1).price(12.99).build();
		IProduct product2 = BasketProduct.builder().barcode("A0002").name("Product 2").quantity(2).price(3.99).build();
		IProduct product3 = BasketProduct.builder().barcode("A0003").name("Product 3").quantity(5).price(0.99).build();

		IDiscount discount10Percent = ChallengeDiscount.builder().minimumQuantity(1).appliedQuantity(1)
				.discountFactor(0.1).build();

		discount10Percent.addDiscountedProduct(product1.getBarcode());
		discount10Percent.addDiscountedProduct(product1.getBarcode());

		IDiscount discountBuy1Get1Free = ChallengeDiscount.builder().minimumQuantity(2).appliedQuantity(2)
				.discountFactor(0.5).nextDiscount(discount10Percent).build();
		discountBuy1Get1Free.addDiscountedProduct(product2.getBarcode());
		discountBuy1Get1Free.addDiscountedProduct(product3.getBarcode());

		double priceOfAllProducts = product1.getQuantity() * product1.getPrice() * 0.9
				+ product2.getQuantity() * product2.getPrice() * 0.5 + 4 * product3.getPrice() * 0.5
				+ 1 * product3.getPrice();

		Map<String, IProduct> testInventory = new HashMap<>();
		testInventory.put(product1.getBarcode(), product1);
		testInventory.put(product2.getBarcode(), product2);
		testInventory.put(product3.getBarcode(), product3);

		IBasket testBasket = Basket.builder().inventory(testInventory).discountChain(discountBuy1Get1Free).build();

		// when
		double totalPrice = testBasket.total();

		// then
		assertThat(testBasket.getInventory().size()).isEqualTo(3);
		assertThat(totalPrice).isEqualTo(priceOfAllProducts);
	}

}
