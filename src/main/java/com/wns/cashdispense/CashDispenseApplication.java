package com.wns.cashdispense;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.wns.cashdispense.controller.CashController;
import com.wns.cashdispense.exception.CurrencyCombinationException;
import com.wns.cashdispense.exception.InsufficientFundsException;
import com.wns.cashdispense.util.Note;

@SpringBootApplication
public class CashDispenseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CashDispenseApplication.class, args);

		Scanner input = new Scanner(System.in);
		boolean isRunning = true;
		final String help = "Usage Options : \n\tTo Withdraw Amount Press 1 \n\tTo Get Report Press 2 \n\tTo Exit Press 0\n";

		CashController cashController = new CashController();

		int count;
		// Add
		try {
			for (Note denomination : Note.values()) {
				System.out.printf("Enter the number of %s :  ", denomination.toString());
				count = input.nextInt();
				cashController.add(denomination, count);
			}
			System.out.println(help);

			System.out.println("Please select one Option");

			while (isRunning) {
				String opts = input.nextLine();
				try {
					switch (opts.trim()) {
					case "0":
						System.out.println(" Your Statement :");
						isRunning = false;
						break;
					case "1":
						System.out.println(" Enter withdrawal amount :");
						int withDrawAmt = input.nextInt();
						BigDecimal amount = BigDecimal.valueOf(withDrawAmt);
						cashController.withdraw(amount);
						System.out.println(" Successfully Processed !");
						break;
					case "2":
						System.out.println("Available Fund:");
						cashController.reportAll();
						break;
					}
				} catch (IllegalStateException | IllegalArgumentException | CurrencyCombinationException
						| InsufficientFundsException | InputMismatchException e) {
					System.out.println(e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();

				}

			}
		} catch (Exception e) {
			System.out.println(" Invalid Entry, Please try again...");
		}

	}

}
