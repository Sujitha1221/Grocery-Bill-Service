package grocerybillservicepublisher;

import java.util.Scanner;

public interface BillServicePublish {
	public void addProductDetails(int productCode, String productName, Double quantity, Double price);
	
	public void calculateSubTotal(Double quantity, Double price);
	
	public Double calulateTotalBill();
	
	public String displayProductList();
	
	public void clearAllLists();
	
	public Double calculateBalance(Double totalAmount, Double paidAmount);
	
	public String getBill(Double totalAmount, Double paidAmount);
	
	public double convertToStandardQuantity(int quantity, String unitOfMeasurement);
	
	public Double calculateTotalAfterDiscount(Double total);
	
	public int calculateLoyaltyPoints(double totalAmount);

	public void initializeLoyaltyMembers();
	
	public void inputProductDetails(Scanner scanner, BillServicePublish servicePublish);
	
	public boolean authenticateAdmin();
	
	public void initializeAdminCredentials();
	
	public void writeBillDetailsToFile(double amountAfterDiscount, double paidAmount, double balance);
	
}
