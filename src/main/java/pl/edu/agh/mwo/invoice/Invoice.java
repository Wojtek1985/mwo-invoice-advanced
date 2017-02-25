package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
	private Map<Product,Integer> products;
	private static long nextID;
	private long ID;
	
	public Invoice() {
		ID=nextID;
		nextID++;
		products=new HashMap<Product,Integer>();
	}
	public static void resetID() {
		nextID=1;
	}
	
	public String printInvoice() {
		StringBuilder s=new StringBuilder("Faktura nr "+ID+"\n");
		s.append("***********************\n");
		
		for (Product p:products.keySet()) {
			
			String ptype=p.getClass().getSimpleName();
			String pname=p.getName();
			String pprice=p.getPrice().toString();
			String pquantity=products.get(p).toString();
			s.append(ptype+" "+pname+" "+pprice+" "+pquantity+"\n");
		}
		s.append("***********************\n");
		s.append("Wartosc netto:"+getNetTotal()+"\n");
		s.append("Podatek:"+getTax()+"\n");
		s.append("Wartosc brutto:"+getTotal()+"\n");
		s.append("Liczba pozycji:"+products.size()+"\n");
		s.append("***********************\n");
		
		return s.toString();
	}
	
	public void addProduct(Product product) {
		addProduct(product,1);
	}

	public void addProduct(Product product, Integer quantity) {
		if (product==null) throw new IllegalArgumentException();
		if (quantity<1) throw new IllegalArgumentException();
		boolean duplicate=false;
		Product other=null;
		for (Product p:products.keySet()) {
			if (p.getName().equals(product.getName())) {
				duplicate=true;
				other=p;
			}
		}
		if (duplicate) {
			int q=products.get(other);
			q=q+quantity;
			products.put(other, quantity);
		}
		else {
			products.put(product,quantity);
		}
	}

	public BigDecimal getNetTotal() {
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
		BigDecimal subt=getNetTotal();
		BigDecimal total=subt.add(getTax());
		return total;
	}
	
	public long getID() {
		// TODO Auto-generated method stub
		return ID;
	}
	
}
