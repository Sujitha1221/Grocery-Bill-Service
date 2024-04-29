package grocerybillsubscriberservice;

import grocerybillservicepublisher.BillServicePublish;

import java.util.HashMap;
import java.util.Scanner;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class SubscriberActivator implements BundleActivator {

	ServiceReference serviceReference;
	HashMap<String, Integer> loyaltyMembers; 
	
	public void start(BundleContext bundleContext) throws Exception {
        serviceReference = bundleContext.getServiceReference(BillServicePublish.class.getName());
        BillServicePublish servicePublish = (BillServicePublish)bundleContext.getService(serviceReference);
        loyaltyMembers = new HashMap<>();
        
        servicePublish.initializeLoyaltyMembers(); 
        
        System.out.println("\n\tStart Grocery Shop Bill Subscriber Service\n");
        Scanner scanner = new Scanner(System.in);
        boolean continueAdding = true;
        
        if (servicePublish.authenticateAdmin()) {

        System.out.print("\nAre you a loyalty member? (Y/N): ");
        String loyaltyChoice = scanner.next();
        boolean isLoyaltyMember = loyaltyChoice.equalsIgnoreCase("y");
        int loyaltyPoints = 0;
        String loyaltyIdentifier = null; // To store the loyalty member's identifier

        if (isLoyaltyMember) {
            System.out.print("Enter your loyalty identifier (phone number or email): ");
            loyaltyIdentifier = scanner.next();

            if (loyaltyMembers.containsKey(loyaltyIdentifier)) {
                loyaltyPoints = loyaltyMembers.get(loyaltyIdentifier);
                System.out.println("Welcome back! Your current loyalty points: " + loyaltyPoints);
            }else {
                loyaltyMembers.put(loyaltyIdentifier, 0); // Initialize points to 0
                System.out.println("Welcome to our loyalty program!");
            }
        } else {
            System.out.println("Would you like to join our loyalty program? (Y/N): ");
            String joinChoice = scanner.next();
            if (joinChoice.equalsIgnoreCase("y")) {
                System.out.print("Enter your identifier (phone number or email) to join: ");
                loyaltyIdentifier = scanner.next();
                loyaltyMembers.put(loyaltyIdentifier, 0); 
                System.out.println("Welcome to our loyalty program! You have been enrolled as a new member.");
            }
        }
        
        while (continueAdding) {
        	servicePublish.inputProductDetails(scanner, servicePublish);

            System.out.print("Do you want to add more products? (Y/N): ");
            String choice = scanner.next();
            continueAdding = choice.equalsIgnoreCase("y");
        }
        double totalAmount = servicePublish.calulateTotalBill();
        System.out.println("\nTotal Cost For All The Products:  Rs " + totalAmount);
        
        double amountAfterDiscount = servicePublish.calculateTotalAfterDiscount(totalAmount);
        System.out.println("\nThe amount you have to pay is :  Rs " + amountAfterDiscount);
        
        if (isLoyaltyMember && loyaltyIdentifier != null) {
            // Update loyalty points earned
            int newPoints = servicePublish.calculateLoyaltyPoints(totalAmount);
            loyaltyPoints += newPoints;
            loyaltyMembers.put(loyaltyIdentifier, loyaltyPoints); // Update loyalty points for the member
            System.out.println("You earned " + newPoints + " loyalty points. Total points: " + loyaltyPoints);
        }
        
        System.out.print("Enter Customer Paid Amount:  ");
		double paidAmount = scanner.nextDouble();

		servicePublish.calculateBalance( amountAfterDiscount, paidAmount);
		

		System.out.println("\n\t\t\t\tWelcome To Grocery Shop Bill Service");
		
		System.out.println(
				"------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("\n\t\t##### Selected Products And Sub Totals #####\n\n");
		
		System.out.println(servicePublish.displayProductList());
		
		System.out.println("\n\n\t" + servicePublish.getBill(amountAfterDiscount, paidAmount) + "\n\n");

		System.out.println("Loyalty Points Earned: " + loyaltyPoints);
		System.out.println("\nThank you for shopping with us. Come again.");
		System.out.println(
				"------------------------------------------------------------------------------------------------------------------------------------------------");
		servicePublish.writeBillDetailsToFile(amountAfterDiscount, paidAmount, servicePublish.calculateBalance(amountAfterDiscount, paidAmount));
        
        } else {
            System.out.println("Invalid admin credentials. Exiting bill process.");
            System.out.println("Have a great day.");
        }
		servicePublish.clearAllLists();
		Thread.sleep(60000);
       
	}

	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println("Stop Subscriber service");
		bundleContext.ungetService(serviceReference);
	}

}
