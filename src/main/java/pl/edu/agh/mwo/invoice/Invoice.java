package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;


import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
	HashMap<Product,Integer> products=new HashMap<Product,Integer>();
	
	public void addProduct(Product product) {
		products.put(product,0);
	}

	public void addProduct(Product product, Integer quantity) {
		products.put(product,quantity);
	}

	public BigDecimal getSubtotal() {
		BigDecimal sum=BigDecimal.ZERO;
		
		
		return sum;
	}

	public BigDecimal getTax() {
		BigDecimal sum=BigDecimal.ZERO;
		
		return sum;
	}

	public BigDecimal getTotal() {
		BigDecimal subt=getSubtotal();
		return subt.add(getTax());
	}
}
