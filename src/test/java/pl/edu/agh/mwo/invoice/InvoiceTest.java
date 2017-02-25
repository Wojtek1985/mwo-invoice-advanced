package pl.edu.agh.mwo.invoice;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.OtherProduct;
import pl.edu.agh.mwo.invoice.product.Product;
import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;

public class InvoiceTest {
	private static final String PRODUCT_1 = "Product 1";
	private static final String PRODUCT_2 = "Product 2";
	private static final String PRODUCT_3 = "Product 3";
	
	@Before
	public void clean() {
		Invoice.resetID();
	}
	
	@Test
	public void testInvoiceHasNumberGreaterThanZero() {
		Invoice invoice=createEmptyInvoice();
		Assert.assertThat(invoice.getID(), Matchers.greaterThan((long)0));
	}
	@Test
	
	public void testDifferentNumbers() {
		Invoice inv1=createEmptyInvoice();
		Invoice inv2=createEmptyInvoice();
		Assert.assertNotEquals(inv1.getID(), inv2.getID());
	}
	
	@Test
	public void testPrintedInvoiceHasNumber() {
		Invoice invoice=createEmptyInvoice();
		String printed=invoice.printInvoice();
		String invoiceNo=Long.toString(invoice.getID());
		Assert.assertThat(printed, Matchers.containsString(invoiceNo));
	}
	
	@Test
	public void testInvoiceHasProductName() {
		Invoice invoice=createEmptyInvoice();
		Product milk=new DairyProduct("mleko", new BigDecimal(3.33));
		invoice.addProduct(milk);
		String printed=invoice.printInvoice();
		Assert.assertThat(printed,Matchers.containsString("mleko"));
	}
	@Test
	public void testInvoiceHasProductPrice() {
		Invoice invoice=createEmptyInvoice();
		Product milk=new DairyProduct("mleko", new BigDecimal(3.33));
		invoice.addProduct(milk);
		String printed=invoice.printInvoice();
		Assert.assertThat(printed,Matchers.containsString("3.33"));
	}
	@Test
	public void testInvoiceHasProductQuantity() {
		Invoice invoice=createEmptyInvoice();
		Product milk=new DairyProduct("mleko", new BigDecimal(3.33));
		invoice.addProduct(milk,888);
		String printed=invoice.printInvoice();
		Assert.assertThat(printed,Matchers.containsString("888"));
	}
	@Test
	public void testNoDuplicates() {
		Invoice invoice=createEmptyInvoice();
		Product milk=new DairyProduct("mleko", new BigDecimal(3));
		Product milk2=new DairyProduct("mleko", new BigDecimal(3));
		
		invoice.addProduct(milk,3);
		invoice.addProduct(milk2,3);
		String printed=invoice.printInvoice();
		Assert.assertThat(printed,Matchers.containsString("Liczba pozycji:1"));
	}
	
	@Test
	public void testInvoiceHasNumberofPositions() {
		Invoice i=createEmptyInvoice();
		Product p1=new DairyProduct("P1",new BigDecimal(1));
		Product p2=new DairyProduct("P2",new BigDecimal(1));
		Product p3=new DairyProduct("P",new BigDecimal(1));
		i.addProduct(p1);
		i.addProduct(p2);
		i.addProduct(p3);
		String printed=i.printInvoice();
		Assert.assertThat(printed, Matchers.containsString("3"));
	}
	
	
	@Test
	public void testNextInvoiceHasSubsequentNumbers() {
		Invoice inv1=createEmptyInvoice();
		Invoice inv2=createEmptyInvoice();
		Assert.assertEquals(1, inv2.getID()-inv1.getID());
	}
	
	@Test
	public void testFirstInvoiceId() {
		Invoice inv=createEmptyInvoice();
		assertEquals(1,inv.getID());
	}
	
	@Test
	public void testThirdInvoiceId() {	
		createEmptyInvoice();
		createEmptyInvoice();
		Invoice inv=createEmptyInvoice();
		assertEquals(3,inv.getID());
	}
	 
	
	@Test
	public void testEmptyInvoiceHasEmptySubtotal() {
		Invoice invoice = createEmptyInvoice();
		assertBigDecimalsAreEqual(BigDecimal.ZERO, invoice.getNetTotal());
	}

	@Test
	public void testEmptyInvoiceHasEmptyTaxAmount() {
		Invoice invoice = createEmptyInvoice();
		assertBigDecimalsAreEqual(BigDecimal.ZERO, invoice.getTax());
	}

	@Test
	public void testEmptyInvoiceHasEmptyTotal() {
		Invoice invoice = createEmptyInvoice();
		assertBigDecimalsAreEqual(BigDecimal.ZERO, invoice.getTotal());
	}

	@Test
	public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 1);
		assertBigDecimalsAreEqual(invoice.getNetTotal(), invoice.getTotal());
	}

	@Test
	public void testInvoiceHasProperSubtotalForManyProduct() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 1);
		invoice.addProduct(createOtherProduct(), 1);
		invoice.addProduct(createDairyProduct(), 1);
		assertBigDecimalsAreEqual("259.99", invoice.getNetTotal());
	}

	@Test
	public void testInvoiceHasProperTaxValueForManyProduct() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 1);
		invoice.addProduct(createOtherProduct(), 1);
		invoice.addProduct(createDairyProduct(), 1);
		assertBigDecimalsAreEqual("12.3", invoice.getTax());
	}

	@Test
	public void testInvoiceHasProperTotalValueForManyProduct() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct());
		invoice.addProduct(createOtherProduct());
		invoice.addProduct(createDairyProduct());
		assertBigDecimalsAreEqual("272.29", invoice.getTotal());
	}

	@Test
	public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 3); // Subtotal: 599.97
		invoice.addProduct(createOtherProduct(), 2); // Subtotal: 100.00
		invoice.addProduct(createDairyProduct(), 4); // Subtotal: 40.00
		assertBigDecimalsAreEqual("739.97", invoice.getNetTotal());
	}

	@Test
	public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 3); // Total: 599.97
		invoice.addProduct(createOtherProduct(), 2); // Total: 123.00
		invoice.addProduct(createDairyProduct(), 4); // Total: 43.2
		assertBigDecimalsAreEqual("766.17", invoice.getTotal());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvoiceWithZeroQuantity() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvoiceWithNegativeQuantity() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), -1);
	}

	private Invoice createEmptyInvoice() {
		return new Invoice();
	}

	private Product createTaxFreeProduct() {
		return new TaxFreeProduct(PRODUCT_1, new BigDecimal("199.99"));
	}

	private Product createOtherProduct() {
		return new OtherProduct(PRODUCT_2, new BigDecimal("50.0"));
	}

	private Product createDairyProduct() {
		return new DairyProduct(PRODUCT_3, new BigDecimal("10.0"));
	}

	private void assertBigDecimalsAreEqual(String expected, BigDecimal actual) {
		assertEquals(new BigDecimal(expected).stripTrailingZeros(), actual.stripTrailingZeros());
	}

	private void assertBigDecimalsAreEqual(BigDecimal expected, BigDecimal actual) {
		assertEquals(expected.stripTrailingZeros(), actual.stripTrailingZeros());
	}

}
