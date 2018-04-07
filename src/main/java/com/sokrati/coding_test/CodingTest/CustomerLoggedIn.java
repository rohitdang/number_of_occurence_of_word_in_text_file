package com.sokrati.coding_test.CodingTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.sokrati.coding_test.exceptions.InternalServerErrorException;
import com.sokrati.coding_test.exceptions.NotFoundException;

public class CustomerLoggedIn {

	public boolean isFourConsecutiveDays(List<Date> dateList) {
		Collections.sort(dateList);
		Calendar c = Calendar.getInstance();
		int numConsecutive = 0;
		Date last = null;
		for (int i = 0; i < dateList.size(); i++) {
			c.setTime(dateList.get(i));
			c.add(Calendar.DATE, -1);
			if (c.getTime().equals(last)) {
				numConsecutive++;
			} else {
				numConsecutive = 0;
			}
			if (numConsecutive == 3) {
				return true;
			}
			last = dateList.get(i);
		}
		return false;
	}

	public Map<String, List<Date>> generateCustomerLoginMap(String filePath) throws InternalServerErrorException, NotFoundException {
		BufferedReader reader = null;
		try {
			File file = new File(filePath);
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String[] arrayOfString = line.split("\t");
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				Date date = sdf.parse(arrayOfString[0]);
				if (customerDetails.containsKey(arrayOfString[1])) {
					customerDetails.get(arrayOfString[1]).add(date);
				}
				else {
					List<Date> listOfDate = new ArrayList<Date>();
					listOfDate.add(date);
					customerDetails.put(arrayOfString[1], listOfDate);
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(),e);
			throw new NotFoundException(e.getMessage(), e);
		}  catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new InternalServerErrorException(e.getMessage(), e);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new InternalServerErrorException(e.getMessage(), e);
		} 
		return customerDetails;
	}

	private static void writeToFile(List<String> listOFCustomers) throws InternalServerErrorException {
		FileWriter fileWritter = null;
		PrintWriter printWritter = null;
		try {
			fileWritter = new FileWriter("./Results/result_for_question2.txt",false);
			printWritter = new PrintWriter(fileWritter, false);
			// write it to file
			printWritter.println("list of those customers that log in on four consecutive days :");
			for (String listOFCustomer : listOFCustomers) {	
				printWritter.println(listOFCustomer);
			}
		}
		catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new InternalServerErrorException(e.getMessage(), e);
		}
		finally {
			if(printWritter != null)
				printWritter.flush();
			printWritter.close();
			try {
				if(fileWritter != null)
					fileWritter.close();
			} catch (IOException e) {
				log.error(e.getMessage(), e);
				throw new InternalServerErrorException(e.getMessage(), e);
			}
		}
	}

	public List<String> getListOfRequiredUserIds(Map<String, List<Date>> map) {
		System.out.println("list of those customers that log in on four consecutive days : ");
		List<String> listOfCustomersLoggedInFor4ConssecutiveDays = new ArrayList<String>();
		for (Entry<String, List<Date>> entry : map.entrySet()) {
			List<Date> listOfDates = entry.getValue();
			if(isFourConsecutiveDays(listOfDates) == true) {
				System.out.println(entry.getKey());
				listOfCustomersLoggedInFor4ConssecutiveDays.add(entry.getKey());
			}
		}
		return listOfCustomersLoggedInFor4ConssecutiveDays;
	}

	public static void main(String[] args) throws InternalServerErrorException, NotFoundException {
		BasicConfigurator.configure();
		CustomerLoggedIn cli =  new CustomerLoggedIn();
		String filePath = null;
		try {
			ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			filePath = classloader.getResource("customer-details.txt").getPath();
		} catch(Exception e) {
			log.error(e.getMessage(),e);
			throw new NotFoundException(e.getMessage(), e);
		}
		List<String> listOFCustomers = cli.getListOfRequiredUserIds(cli.generateCustomerLoginMap(filePath));
		writeToFile(listOFCustomers);
	}
	
	Map<String, List<Date>> customerDetails = new HashMap<String, List<Date>>();
	public static Logger log = Logger.getLogger(CustomerLoggedIn.class.getName());
}
