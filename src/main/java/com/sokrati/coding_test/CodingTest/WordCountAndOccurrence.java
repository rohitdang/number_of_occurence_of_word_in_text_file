package com.sokrati.coding_test.CodingTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.sokrati.coding_test.exceptions.InternalServerErrorException;
import com.sokrati.coding_test.exceptions.NotFoundException;

public class WordCountAndOccurrence implements Comparable<WordCountAndOccurrence> {

	public WordCountAndOccurrence(String word) {
		this.word = word;
		addOccurrence();
	}

	public final void addOccurrence() {
		totalCount++;
	}

	@Override
	public int compareTo(WordCountAndOccurrence wordOccurrence) {
		return totalCount - wordOccurrence.totalCount;
	}

	@Override
	public String toString() {
		return word + " = " + totalCount;
	}

	public void countOccurrencesAndStoreInfoInFile(String line, String filePath) throws InternalServerErrorException, NotFoundException {
		TreeMap<String, WordCountAndOccurrence> words = new TreeMap<>();
		File file = new File(filePath);
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(file)));

			while ((line = reader.readLine()) != null) {
				Scanner scanner = new Scanner(file);
				while (scanner.hasNextLine()) {
					String nextToken = scanner.nextLine();
					if (nextToken.contains(line)) {
						if (words.containsKey(line)) {
							words.get(line).addOccurrence();
						}
						else {
							words.put(line, new WordCountAndOccurrence(line));
						}
					}
				}
				scanner.close();
			}
			reader.close();
		} catch (FileNotFoundException e) {
			log.error(e.getMessage(),e);
			throw new NotFoundException(e.getMessage(), e);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new InternalServerErrorException(e.getMessage(), e);
		} 

		displayInOrderOfOccurrence(words);
	}

	public void displayInOrderOfOccurrence(
			Map<String, WordCountAndOccurrence> words) throws InternalServerErrorException {

		List<WordCountAndOccurrence> orderedByOccurrence = new ArrayList<>();

		// sort
		for (Map.Entry<String, WordCountAndOccurrence> entry : words.entrySet()) {
			WordCountAndOccurrence wordOccurrence = entry.getValue();

			// initialize the list on the first round
			if (orderedByOccurrence.isEmpty()) {
				orderedByOccurrence.add(wordOccurrence);
			} else {
				for (int i = 0; i < orderedByOccurrence.size(); i++) {
					if (wordOccurrence.compareTo(orderedByOccurrence.get(i)) > 0) {
						orderedByOccurrence.add(i, wordOccurrence);
						break;
					} else if (i == orderedByOccurrence.size() - 1) {
						orderedByOccurrence.add(wordOccurrence);
						break;
					}
				}
			}
		}
		writeToAFile(orderedByOccurrence);
	}

	private void writeToAFile(List<WordCountAndOccurrence> orderedByOccurrence) throws InternalServerErrorException {
		FileWriter fileWritter = null;
		PrintWriter printWritter = null;

		try {
			fileWritter = new FileWriter("./Results/result_for_question1.txt",false);
			printWritter = new PrintWriter(fileWritter, false);
			// write it to file
			for (WordCountAndOccurrence wordOccurrence : orderedByOccurrence) {	
				printWritter.println(wordOccurrence);
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

	public static void main( String[] args ) throws InternalServerErrorException, NotFoundException	{
		BasicConfigurator.configure();
		String word = null;
		String filePath = null;
		try {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		filePath = classloader.getResource("taxomy.txt").getPath();
		} catch(Exception e) {
			log.error(e.getMessage(),e);
			throw new NotFoundException(e.getMessage(), e);
		}
		
		WordCountAndOccurrence wordCountAndOccurrence = new WordCountAndOccurrence(word);
		wordCountAndOccurrence.countOccurrencesAndStoreInfoInFile(word, filePath);
	}

	public static Logger log = Logger.getLogger(WordCountAndOccurrence.class.getName());
	private final String word;
	private int totalCount = 0;
}