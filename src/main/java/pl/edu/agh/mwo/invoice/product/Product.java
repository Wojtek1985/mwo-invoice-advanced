package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product {
	private final String name;

	private final BigDecimal price;

	private final BigDecimal taxPercent;

	protected Product(String name, BigDecimal price, BigDecimal tax) {
		if (name==null) throw new IllegalArgumentException();
		if (name.equals("")) throw new IllegalArgumentException();
		if (price==null) throw new IllegalArgumentException();
		if (price.signum()==-1) throw new IllegalArgumentException();
		this.price = price;
		this.name = name;
		this.taxPercent = tax;
	}

	public String getName() {
		return this.name;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public BigDecimal getTaxPercent() {
		return this.taxPercent;
	}

	public BigDecimal getPriceWithTax() {
		BigDecimal result=price.add(price.multiply(taxPercent));
		return result;
	}
}
