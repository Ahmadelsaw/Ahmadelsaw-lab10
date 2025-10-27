import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files


public class Lab10_Tester {

  public String getFileContents(String filename){
    String data = "";
    try {
      File myObj = new File(filename);
      Scanner myReader = new Scanner(myObj);
      while (myReader.hasNextLine()) {
        data = myReader.nextLine();
      }
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("could not open " + filename);
      e.printStackTrace();
    }
    return data;
  }

  @Test
  public void test1() {
    System.out.println("process long text with found stopword");
    String expected = "7";
    StringBuffer input = new StringBuffer("This sentence is long enough to yellow pass this test. But, it could be -- even -- longer...");
    String result = "error";
    try{
      result = "" + WordCounter.processText(input, "yellow");
    } catch (Exception e) {}
    
    assertEquals(expected, result);
  }

  @Test
  public void test2() {
    System.out.println("process long text with no stopword");
    String expected = "16";
    StringBuffer input = new StringBuffer("This sentence is long enough to yellow pass this test. But, it could be -- even -- longer...");
    String result = "error";
    try{
      result = "" + WordCounter.processText(input, null);
    } catch (Exception e) {}
    assertEquals(expected, result);
  }

  @Test
  public void test3() {
    System.out.println("process long text with missing stopword");
    StringBuffer input = new StringBuffer("This sentence is long enough to yellow pass this test. But, it could be -- even -- longer...");
    String expected = "InvalidStopwordException: Couldn't find stopword: red";
    String result = "InvalidStopwordException not raised";
    try {
      WordCounter.processText(input, "red");
    } catch (InvalidStopwordException e){
      result = e.toString();
    } catch (Exception e) {}
    assertEquals(expected, result);
  }

  @Test
  public void test4() {
    System.out.println("process short text with found stopword");
    StringBuffer input = new StringBuffer("This red is");
    String expected = "TooSmallText: Only found 3 words.";
    String result = "TooSmallText not raised";
    try {
      WordCounter.processText(input, "red");
    } catch (TooSmallText e){
      result = e.toString();
    } catch (Exception e) {}
    assertEquals(expected, result);
  }

  @Test
  public void test5() {
    System.out.println("process short text with missing stopword");
    StringBuffer input = new StringBuffer("This yellow is");
    String expected = "TooSmallText: Only found 3 words.";
    String result = "TooSmallText not raised";
    try {
      WordCounter.processText(input, "red");
    } catch (TooSmallText e){
      result = e.toString();
    } catch (Exception e) {}
    assertEquals(expected, result);
  }

  @Test
  public void test6() {
    System.out.println("process short text with found stopword");
    StringBuffer input = new StringBuffer("This yellow is");
    String expected = "TooSmallText: Only found 3 words.";
    String result = "TooSmallText not raised";
    try {
      WordCounter.processText(input, "yellow");
    } catch (TooSmallText e){
      result = e.toString();
    } catch (Exception e) {}
    assertEquals(expected, result);
  }

  @Test
  public void test7() {
    System.out.println("process easy long text with end stopword");
    String expected = "10";
    StringBuffer input = new StringBuffer("This sentence is long enough to pass this test. yellow.");
    String result = "error";
    try{
      result = "" + WordCounter.processText(input, "yellow");
    } catch (Exception e) {}
    assertEquals(expected, result);
  }

  @Test
  public void test8() {
    System.out.println("process found file");
    String expected = "This file has enough words to not trigger and exception and the stopword we're going to use " +
        "is yellow so we shouldn't have scanned into this point -- it just isn't necessary...unless " +
        "the stopword we wanted was green in which case we stopped above. Or, perhaps no stopword was provided, " + 
        "so then we will read in the whole file. ";
    try {
      String strCurrentLine = null;
      BufferedWriter file = new BufferedWriter(new FileWriter("valid.txt"));
      file.write(expected);
      file.close();
    } catch (IOException err1) { }
    StringBuffer result = new StringBuffer("error");
    try{
      result = WordCounter.processFile("valid.txt");
    } catch (Exception e) {}
    assertEquals(expected, result.toString());
  }

  @Test
  public void test9() {
    System.out.println("process missing file");
    String expected = "This file has enough words to not trigger and exception and the stopword we're going to use " +
        "is yellow so we shouldn't have scanned into this point -- it just isn't necessary...unless " +
        "the stopword we wanted was green in which case we stopped above. Or, perhaps no stopword was provided, " + 
        "so then we will read in the whole file. ";

    InputStream sysInBackup = System.in; // backup System.in to restore it later
    ByteArrayInputStream in = new ByteArrayInputStream("valid.txt".getBytes());
    System.setIn(in);
    StringBuffer result = new StringBuffer("error");
    try{
      result = WordCounter.processFile("invalid.txt");
    } catch (Exception e) {}
    assertEquals(expected, result.toString());
    System.setIn(sysInBackup); // optionally, reset System.in to its original
  }

  @Test
  public void test10() {
    System.out.println("process empty file");
    try {
      String strCurrentLine = null;
      BufferedWriter file = new BufferedWriter(new FileWriter("empty.txt"));
      String expected = "";
      file.write(expected);
      file.close();
    } catch (IOException err1) { }

    String result = "failed to catch EmptyFileException";
    try {
      WordCounter.processFile("empty.txt");
    } catch (EmptyFileException e){
      result = e.toString();
    } catch (Exception e){
      result = "error";
    }
    assertEquals("EmptyFileException: empty.txt was empty", result);
  }

    @Test
  public void test11() {
    System.out.println("test main valid number choice 1 with stopword");
    try {
      String strCurrentLine = null;
      BufferedWriter file = new BufferedWriter(new FileWriter("file1.txt"));
      String expected = "items that are greater than five yes words";
      file.write(expected);
      file.close();
    } catch (IOException err1) { }

    String result = "failed to ask for a new option";
    try {
      InputStream sysInBackup = System.in; // backup System.in to restore it later
      ByteArrayInputStream in = new ByteArrayInputStream("1".getBytes());
      System.setIn(in);

      PrintStream sysOutBackup   = null;
      PrintStream output  = null;
      try {
        //Process process = Runtime.getRuntime().exec("rm out.txt");
        sysOutBackup = System.out; // Saving the original System.out to restore it later
        File f1 = new File("out.txt");
        if(f1.exists()) { 
            f1.delete();
        }
        output = new PrintStream(new FileOutputStream("out.txt",true));
        System.setOut(output); // Redirecting console output to file
        System.setErr(output);// Redirecting runtime exceptions to file
      }
      catch (Exception e)
      {
        System.out.println("error in output redirection");
        e.printStackTrace();
      }

      String[] args = {"file1.txt", "than"};
      WordCounter.main(args);
      System.setOut(sysOutBackup); // optionally, reset System.in to its original
      System.setIn(sysInBackup); // optionally, reset System.in to its original
      result = getFileContents("out.txt");
      
    } catch (Exception e){
      result = "error";
      e.printStackTrace();
    } 

    String expected = "Found 5 words.";
    assertEquals(expected, result);
  }

  @Test
  public void test12() {
    System.out.println("test main valid number choice 1 no stopword");
    try {
      String strCurrentLine = null;
      BufferedWriter file = new BufferedWriter(new FileWriter("file1.txt"));
      String expected = "items that need counted are greater than five words";
      file.write(expected);
      file.close();
    } catch (IOException err1) { }

    String result = "failed to ask for a new option";
    try {
      InputStream sysInBackup = System.in; // backup System.in to restore it later
      ByteArrayInputStream in = new ByteArrayInputStream("1".getBytes());
      System.setIn(in);

      PrintStream sysOutBackup   = null;
      PrintStream output  = null;
      try {
        sysOutBackup = System.out; // Saving the original System.out to restore it later
        File f1 = new File("out.txt");
        if(f1.exists()) { 
            f1.delete();
        }
        output = new PrintStream(new FileOutputStream("out.txt",true));
        System.setOut(output); // Redirecting console output to file
        System.setErr(output);// Redirecting runtime exceptions to file
      }
      catch (Exception e)
      {
        System.out.println("error in output redirection");
        e.printStackTrace();
      }

      String[] args = {"file1.txt"};
      WordCounter.main(args);
      System.setOut(sysOutBackup); // optionally, reset System.in to its original
      System.setIn(sysInBackup); // optionally, reset System.in to its original
      result = getFileContents("out.txt");
      
    } catch (Exception e){
      result = "error";
      e.printStackTrace();
    } 

    String expected = "Found 9 words.";
    assertEquals(expected, result);
  }

  @Test
  public void test13() {
    System.out.println("test main valid number choice 1 empty file");
    try {
      String strCurrentLine = null;
      BufferedWriter file = new BufferedWriter(new FileWriter("file1.txt"));
      String expected = "";
      file.write(expected);
      file.close();
    } catch (IOException err1) { }

    String result = "failed to ask for a new option";
    try {
      InputStream sysInBackup = System.in; // backup System.in to restore it later
      ByteArrayInputStream in = new ByteArrayInputStream("1".getBytes());
      System.setIn(in);

      PrintStream sysOutBackup   = null;
      PrintStream output  = null;
      try {
        sysOutBackup = System.out; // Saving the original System.out to restore it later
        File f1 = new File("out.txt");
        if(f1.exists()) { 
            f1.delete();
        }
        output = new PrintStream(new FileOutputStream("out.txt",true));
        System.setOut(output); // Redirecting console output to file
        System.setErr(output);// Redirecting runtime exceptions to file
      }
      catch (Exception e)
      {
        System.out.println("error in output redirection");
        e.printStackTrace();
      }

      String[] args = {"file1.txt"};
      WordCounter.main(args);
      System.setOut(sysOutBackup); // optionally, reset System.in to its original
      System.setIn(sysInBackup); // optionally, reset System.in to its original
      result = getFileContents("out.txt");
      System.out.println("this is what we got from main:\n" + result);
      
    } catch (Exception e){
      result = "error";
      e.printStackTrace();
    } 

    String expected = "TooSmallText: Only found 0 words.";
    assertEquals(expected, result);
  }

  @Test
  public void test14() {
    System.out.println("test main valid number choice 1 empty file");
    try {
      String strCurrentLine = null;
      BufferedWriter file = new BufferedWriter(new FileWriter("file1.txt"));
      String expected = "shor tt";
      file.write(expected);
      file.close();
    } catch (IOException err1) { }

    String result = "failed to ask for a new option";
    try {
      InputStream sysInBackup = System.in; // backup System.in to restore it later
      ByteArrayInputStream in = new ByteArrayInputStream("1".getBytes());
      System.setIn(in);

      PrintStream sysOutBackup   = null;
      PrintStream output  = null;
      try {
        sysOutBackup = System.out; // Saving the original System.out to restore it later
        File f1 = new File("out.txt");
        if(f1.exists()) { 
            f1.delete();
        }
        output = new PrintStream(new FileOutputStream("out.txt",true));
        System.setOut(output); // Redirecting console output to file
        System.setErr(output);// Redirecting runtime exceptions to file
      }
      catch (Exception e)
      {
        System.out.println("error in output redirection");
        e.printStackTrace();
      }

      String[] args = {"file1.txt"};
      WordCounter.main(args);
      System.setOut(sysOutBackup); // optionally, reset System.in to its original
      System.setIn(sysInBackup); // optionally, reset System.in to its original
      result = getFileContents("out.txt");
      System.out.println("this is what we got from main:\n" + result);
      
    } catch (Exception e){
      result = "error";
      e.printStackTrace();
    } 

    String expected = "TooSmallText: Only found 2 words.";
    assertEquals(expected, result);
  }

}
