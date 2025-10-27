import java.io.*;
import java.util.*;
import java.util.regex.*;

public class WordCounter {

    public static int processText(StringBuffer text, String stopword) throws InvalidStopwordException, TooSmallText {
        if (text == null) {
            text = new StringBuffer("");
        }

        Pattern regex = Pattern.compile("[a-zA-Z0-9']+");
        Matcher matcher = regex.matcher(text);

        int totalCount = 0;
        int countThroughStop = 0;
        boolean stopFound = false;

        while (matcher.find()) {
            String word = matcher.group();
            totalCount++;
            if (!stopFound) {
                countThroughStop++;
                if (stopword != null && word.equalsIgnoreCase(stopword)) {stopFound = true;}
            }
        }

        if (totalCount < 5) {
            throw new TooSmallText("Only found " + totalCount + " words.");
        }

        if (stopword != null && !stopFound) {
            throw new InvalidStopwordException("Couldn't find stopword: " + stopword);
        }
        if (stopword != null) {
            return countThroughStop;
        } else {
            return totalCount;
        }
    }
    
    public static StringBuffer processFile(String path) throws EmptyFileException {
        return processFile(path, new Scanner(System.in));
    }

    public static StringBuffer processFile(String path, Scanner input) throws EmptyFileException {
        File file = new File(path);
        Scanner fileScanner = null;

        while (true) {
            try {
                fileScanner = new Scanner(file);
                break;
            } catch (FileNotFoundException e) {
                System.err.println("File not found. Enter another filename:");
                // Use the passed-in Scanner for console input
                path = input.nextLine(); 
                file = new File(path);
            }
        }

        StringBuilder sb = new StringBuilder();
        boolean sawAny = false;
        
        while (fileScanner.hasNextLine()) {
            sawAny = true;
            sb.append(fileScanner.nextLine());
            if (fileScanner.hasNextLine()) {
                sb.append('\n'); 
            }
        }
        fileScanner.close();

        if (!sawAny) {
            throw new EmptyFileException(path + " was empty");
        }
        
        return new StringBuffer(sb.toString());
    }

    public static void main(String[] args) {
        // This is the ONE AND ONLY Scanner attached to System.in for the main loop
        Scanner input = new Scanner(System.in);

        //Get option
        Integer optFromArgs = null;
        if (args.length >= 1) {
            try {
                int tempOpt = Integer.parseInt(args[0]);
                if (tempOpt == 1 || tempOpt == 2) {optFromArgs = tempOpt;}
            } catch (NumberFormatException ignored) {}
        }

        int option;
        if (optFromArgs != null) {
            option = optFromArgs;
        } else {
            while (true) {
                System.err.println("Choose: 1 = process file, 2 = process text");
                try {
                    option = Integer.parseInt(input.nextLine());
                    if (option == 1 || option == 2) break;
                } catch (Exception e) { /* ignore and re-prompt */ }
            }
        }

        //Stopword
        String stopword = (args.length > 1) ? args[1] : null;

        StringBuffer text = new StringBuffer("");
        String initialPath = (args.length > 0 && option == 1) ? args[0] : null;

        try {
            if (option == 1) {
                String path;
                if (initialPath != null) {
                    path = initialPath;
                } else {
                    System.err.print("Enter filename: ");
                    path = input.nextLine();
                }
                
                // CALL THE OVERLOADED VERSION WIth the Scanner
                text = processFile(path, input); 
            } else {
                // text mode
                if (args.length >= 3) {
                    text = new StringBuffer(args[2]);
                } else {
                    System.out.println("Enter your text:");
                    text = new StringBuffer(input.nextLine());
                }
            }

            int count = processText(text, stopword);
            System.out.println("Found " + count + " words.");

        } catch (EmptyFileException e) {
            System.out.println(e);
            try {
                processText(new StringBuffer(""), stopword); 
            } catch (TooSmallText t) {
                System.out.println(t);
            } catch (InvalidStopwordException ignored) {
                // Ignore invalidstopword here as toodmalltext is expected
            }
        } catch (InvalidStopwordException e) {
            // Retry logic
            System.out.println(e);
            System.err.println("Enter another stopword:");
            stopword = input.nextLine(); 
            try {
                int count = processText(text, stopword); 
                System.out.println("Found " + count + " words.");
            } catch (TooSmallText ex) { 
                System.out.println(ex);
            } catch (InvalidStopwordException ex) {
                System.out.println(ex);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        } catch (TooSmallText e) {
            System.out.println(e);
        }
    }
}