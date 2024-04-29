package grocerybillservicepublisher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Scanner;

public class BillServicePublishImpl implements BillServicePublish {

	ArrayList<Integer> ProductCodeList = new ArrayList<Integer>();
	ArrayList<String> ProductNameList = new ArrayList<String>();
	ArrayList<Double> ProductQuantityList = new ArrayList<Double>();
	ArrayList<Double> ProductPriceList = new ArrayList<Double>();
	
	ArrayList<Double> SubTotalList = new ArrayList<Double>();
	HashMap<String, Integer> loyaltyMembers;
	HashMap<String, String> adminCredentials;
	
	@Override
	public void addProductDetails(int productCode, String productName, Double quantity, Double price) {
		ProductCodeList.add(productCode);
		ProductNameList.add(productName);
		ProductPriceList.add(price);
		ProductQuantityList.add(quantity);
		
	}

	@Override
	public void calculateSubTotal(Double quantity, Double price) {
		Double subTotal = quantity*price;
		
		SubTotalList.add(subTotal);
	}

	@Override
	public Double calulateTotalBill() {
		double Total =0.0;
		for (Double subTotal : SubTotalList) {
			Total += subTotal;
		}
		return Total;
	}
	
	@Override
	public Double calculateTotalAfterDiscount(Double total) {
		if(total > 10000) {
			total *= 0.85;
			System.out.println("Congratulations! You have got 15% discount");
		}else if (total > 5000) {
			total *= 0.90;
			System.out.println("Congratulations! You have got 10% discount");
		}else if (total>2000) {
			total *= 0.95;
			System.out.println("Congratulations! You have got 5% discount");
		}
		return total;
	}

	@Override
	public String displayProductList() {
	    StringBuilder productListBuilder = new StringBuilder();

	    productListBuilder.append(String.format("%28s %22s %18s %20s %30s\n", "Product Code", "Product Name", "Unit Price (Rs)", "Quantity", "Sub Total (Rs)\n"));

	    for (int i = 0; i < ProductCodeList.size(); i++) {
	        productListBuilder.append(String.format("%27s %20s %16s %22s %25s\n", ProductCodeList.get(i), ProductNameList.get(i), ProductPriceList.get(i), ProductQuantityList.get(i), SubTotalList.get(i)));
	    }

	    return productListBuilder.toString();
	}


	@Override
	public void clearAllLists() {
		ProductCodeList.clear();
		ProductNameList.clear();
		ProductQuantityList.clear();
		ProductPriceList.clear();
	}

	@Override
	public Double calculateBalance(Double totalAmount, Double paidAmount) {
		Double balance = paidAmount - totalAmount;
		return balance;
	}

	@Override
	public String getBill(Double totalAmount, Double paidAmount) {
	    Double balance = calculateBalance(totalAmount, paidAmount);
	    String message = "Total cost for the products: " + totalAmount + "\n";
	    message += "Paid amount: " + paidAmount + "\n";
	    message += "Balance: " + balance;
	    return message;
	}

	@Override
	public double convertToStandardQuantity(int quantity, String unitOfMeasurement) {

	    double conversionFactor = 1.0;
	    switch (unitOfMeasurement.toLowerCase()) {
	        case "g":
	            conversionFactor = 0.001; // Convert grams to kg
	            break;
	        case "ml":
	            conversionFactor = 0.001; // Convert milliliters to liters
	            break;
	        default:
	            conversionFactor = 1.0; // Default to the same unit of measurement
	            break;
	    }
	    return quantity * conversionFactor;
	}
	
	@Override
	public int calculateLoyaltyPoints(double totalAmount) { 
        return (int) (totalAmount / 100);
    }
	
	@Override
	public void initializeAdminCredentials() {
	    adminCredentials.put("Joseph", "J123");
	    adminCredentials.put("Mariam", "M678");
	}
	
	@Override
	public boolean authenticateAdmin() {
		adminCredentials = new HashMap<>();
	    initializeAdminCredentials();
	    Scanner scanner = new Scanner(System.in);
	    System.out.print("Enter admin username: ");
	    String adminIdentifier = scanner.next();
	    if (adminCredentials.containsKey(adminIdentifier)) {
	        System.out.print("Enter admin passcode: ");
	        String passcode = scanner.next();
	        if (passcode.equals(adminCredentials.get(adminIdentifier))) {
	            return true;
	        } else {
	            System.out.println("Invalid passcode.");
	            return false;
	        }
	    } else {
	        System.out.println("Admin not found.");
	        return false;
	    }
	}
	

	@Override
	public void initializeLoyaltyMembers() {
        loyaltyMembers = new HashMap<>();
        loyaltyMembers.put("Kandu@gmail.com", 100); 
        loyaltyMembers.put("Mary@gmail.com", 50); 
    }
	
	@Override
	 public void inputProductDetails(Scanner scanner, BillServicePublish servicePublish) {
	        System.out.println("Enter product code :");
	        int productCode = scanner.nextInt();

	        System.out.println("Enter product name :");
	        String productName = scanner.next();

	        System.out.println("Enter product quantity :");
	        int quantity = scanner.nextInt();

	        System.out.println("Enter product unit of measurement (e.g., kg, g, l, ml, count(c)):");
	        String unitOfMeasurement = scanner.next();

	        double standardQuantity = 0.0;

	        if (unitOfMeasurement.equalsIgnoreCase("c")) {
	            // If the unit of measurement is 'count', no conversion is needed
	            standardQuantity = quantity;
	        } else {
	            // Convert quantity to standard measurement
	            standardQuantity = servicePublish.convertToStandardQuantity(quantity, unitOfMeasurement);
	        }
	        System.out.println("Enter product price :");
	        double price = scanner.nextDouble();

	        servicePublish.addProductDetails(productCode, productName, standardQuantity, price);
	        servicePublish.calculateSubTotal(standardQuantity, price);
	    }
	
	@Override
	public void writeBillDetailsToFile(double amountAfterDiscount, double paidAmount, double balance) {
	    try (FileWriter fw = new FileWriter("bill_details.txt", true);
	         PrintWriter writer = new PrintWriter(fw)) {
	        LocalDateTime currentDateTime = LocalDateTime.now();
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	        
	        writer.println("Date: " + currentDateTime.format(formatter));
	        writer.println("Bill Amount: Rs " + amountAfterDiscount);
	        writer.println("Total Amount: Rs " + amountAfterDiscount);
	        writer.println("Paid Amount: Rs " + paidAmount);
	        writer.println("Balance: Rs " + balance);
	        writer.println("-----------------------------------------");

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
