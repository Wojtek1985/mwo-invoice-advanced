package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;


import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
	HashMap<Product,Integer> products;
	
	public Invoice() {
		products=new HashMap<Product,Integer>();
	}
	
	public void addProduct(Product product) {
		products.put(product,1);
	}

	public void addProduct(Product product, Integer quantity) {
		if (quantity<1) throw new IllegalArgumentException();
		products.put(product,quantity);
	}

	public BigDecimal getSubtotal() {
		BigDecimal sum=BigDecimal.ZERO;
		for (Product prod:products.keySet()) {
			sum=sum.add(prod.getPrice().multiply(new BigDecimal(products.get(prod))));
		}
		return sum;
	}

	public BigDecimal getTax() {
		BigDecimal sum=BigDecimal.ZERO;
		for (Product prod:products.keySet()) {
			sum=sum.add(prod.getPrice().multiply(prod.getTaxPercent()).multiply(new BigDecimal(products.get(prod))));
		}
		return sum;
	}

	public BigDecimal getTotal() {
		BigDecimal subt=getSubtotal();
		BigDecimal total=subt.add(getTax());
		return total;
	}
}
