package test;

import static org.junit.Assert.*;

import org.junit.Test;

public class TextBuddyTest {
	String fileName = "mytext.txt";
	String expected = "";
	String output = "";

	@Test
	public void testAdd() throws Exception {
		expected = "added to mytext.txt: \"ABC\"";
		output = TextBuddy.runTest(fileName, "add", "ABC");
		assertEquals(expected, output);
	}

	@Test
	public void testDelete() throws Exception {
		expected = "deleted from mytext.txt: \"ABC\"";
		output = TextBuddy.runTest(fileName, "delete", "1");
		assertEquals(expected, output);
	}

	@Test
	public void testDeleteIndexOutOfBound() throws Exception{
		expected = "Input does not exist.";
		output = TextBuddy.runTest(fileName, "delete", "5");
		assertEquals(expected, output);
	}
	

	@Test
	public void testDeleteEmptyInput() throws Exception {
		output = TextBuddy.runTest(fileName, "add", "BCD");
		output = TextBuddy.runTest(fileName, "delete", "5");
		expected = "Input does not exist.";
		assertEquals(expected, output);
	}
	
	@Test
	public void testDisplay() throws Exception{

		output = TextBuddy.runTest(fileName, "display", null);		
		expected = "1. ABC\n2. XYZ\n3. BCD";
		assertEquals(expected, output);
		
	}
	
	@Test
	public void testClear() throws Exception {
		output = TextBuddy.runTest(fileName, "clear", null);
		expected = "all content deleted from mytext.txt.";
		assertEquals(expected, output);
	}
	
	@Test
	public void testSort() throws Exception {
		output = TextBuddy.runTest(fileName, "add", "XYZ");
		output = TextBuddy.runTest(fileName, "sort", null);
		expected = "1. ABC\n2. XYZ";
		assertEquals(expected, output);
	}
	
	
	
	@Test
	public void testSearch() throws Exception {
		
		expected = "2. BCD";
		output = TextBuddy.runTest(fileName, "search", "c");
		assertEquals(expected, output);
	
	}

	@Test
	public void testInvalidSearch() throws Exception {
		
		expected = "Item cannot be found.";
		output = TextBuddy.runTest(fileName, "search", "123");
		assertEquals(expected, output);
	}
}
