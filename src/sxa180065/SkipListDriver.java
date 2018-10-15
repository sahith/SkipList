package sxa180065;

import sxa180065.Timer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

//Driver program for skip list implementation.

public class SkipListDriver {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner sc;
		File file = new File("./lp2-inputs/lp2-in14.txt");
		sc = new Scanner(file);
		String operation = "";
		long operand = 0;
		int modValue = 999983;
		long result = 0;
		Long returnValue = null;
		SkipList<Long> skipList = new SkipList<>();
		// Initialize the timer
		Timer timer = new Timer();

		while (!((operation = sc.next()).equals("End"))) {
			switch (operation) {
			case "Add": {
				operand = sc.nextLong();
				if (skipList.add(operand)) {
					result = (result + 1) % modValue;
				}
				break;
			}
			case "Ceiling": {
				operand = sc.nextLong();
				returnValue = skipList.ceiling(operand);
				if (returnValue != null) {
					// System.out.println("ceiling: " + operand);
					result = (result + returnValue) % modValue;
				}
				break;
			}
			case "First": {
				returnValue = skipList.first();
				if (returnValue != null) {
					// System.out.println("first: " + operand);
					result = (result + returnValue) % modValue;
				}
				break;
			}
			case "Get": {
				int intOperand = sc.nextInt();
				returnValue = skipList.get(intOperand);
				if (returnValue != null) {
					System.out.println("get: " + intOperand + " return: " + returnValue);
					result = (result + returnValue) % modValue;
				}
				break;
			}
			case "Last": {
				returnValue = skipList.last();
				if (returnValue != null) {
					// System.out.println("last: " + operand);
					result = (result + returnValue) % modValue;
				}
				break;
			}
			case "Floor": {
				operand = sc.nextLong();
				returnValue = skipList.floor(operand);
				if (returnValue != null) {
					// System.out.println("floor: " + operand);
					result = (result + returnValue) % modValue;
				}
				break;
			}
			case "Remove": {
				operand = sc.nextLong();
				if (skipList.remove(operand) != null) {
					// System.out.println("remove: " + operand);
					result = (result + 1) % modValue;
				}
				break;
			}
			case "Contains": {
				operand = sc.nextLong();
				if (skipList.contains(operand)) {
					result = (result + 1) % modValue;
				}
				break;
			}

			}
		}

		// End Time
		timer.end();

		System.out.println(result);
		System.out.println(timer);
	}
}