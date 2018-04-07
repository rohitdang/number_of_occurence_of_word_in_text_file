package com.sokrati.codeing_test.CodingTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.junit.Test;

import com.sokrati.coding_test.CodingTest.WordCountAndOccurrence;
import com.sokrati.coding_test.exceptions.InternalServerErrorException;
import com.sokrati.coding_test.exceptions.NotFoundException;

import junit.framework.Assert;

public class WordCountAndOccurrenceTest
{
	@Test
	public void countOccurrencesAndStoreInfoInFileTest() throws InternalServerErrorException, NotFoundException, IOException	{
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		String filePath = classloader.getResource("taxomy_test.txt").getPath();
		String word = null;
		WordCountAndOccurrence wordCountAndOccurrence = new WordCountAndOccurrence(word);
		wordCountAndOccurrence.countOccurrencesAndStoreInfoInFile(word, filePath);
		List<String> listOfLines = new ArrayList<String>();
		File file = new File("./Results/result_for_question1.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));
		while ((word = reader.readLine()) != null) {
			listOfLines.add(word);
		}
		reader.close();
		Assert.assertTrue(listOfLines.contains("Electronics = 14"));
		Assert.assertTrue(listOfLines.contains("Electronics > Arcade Equipment = 7"));
		Assert.assertTrue(listOfLines.contains("Electronics > Arcade Equipment > Video Game Arcade Cabinets = 1"));
		Assert.assertTrue(listOfLines.contains("Electronics > Arcade Equipment > Skee-Ball Machines = 1"));
	}

	@Test(expected=NotFoundException.class)
	public void invalidFileTest() throws InternalServerErrorException, NotFoundException	{
		String word = null;
		BasicConfigurator.configure();
		WordCountAndOccurrence wordCountAndOccurrence = new WordCountAndOccurrence(word);
		wordCountAndOccurrence.countOccurrencesAndStoreInfoInFile(word, "qwerty.txt");
	}
}
