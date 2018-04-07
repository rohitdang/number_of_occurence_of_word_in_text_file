package com.sokrati.codeing_test.CodingTest;

import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.Test;

import com.sokrati.coding_test.CodingTest.CustomerLoggedIn;
import com.sokrati.coding_test.exceptions.InternalServerErrorException;
import com.sokrati.coding_test.exceptions.NotFoundException;

public class CustomerLoggedInTest {
	
	@Test
	public void customerLoggedInDetailsTest() throws InternalServerErrorException, NotFoundException {
		CustomerLoggedIn cli =  new CustomerLoggedIn();
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("customer-details.txt").getPath();
		List<String> listOfCustomers = cli.getListOfRequiredUserIds(cli.generateCustomerLoginMap(filePath));
		Assert.assertTrue(listOfCustomers.contains("0003"));
		Assert.assertTrue(listOfCustomers.contains("0004"));
	}
	
	@Test(expected=NotFoundException.class)
	public void invalidFileTest() throws InternalServerErrorException, NotFoundException	{
		BasicConfigurator.configure();
		CustomerLoggedIn cli =  new CustomerLoggedIn();
		cli.getListOfRequiredUserIds(cli.generateCustomerLoginMap("qwerty.txt"));
	}
}
