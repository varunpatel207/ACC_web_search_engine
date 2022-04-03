package websearchengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

//import java.util.regex.Pattern;

//import textprocessing.BoyerMoore;

public class SearchWord {

  private final int radix; // the radix
  private static int[] right; // the bad-character skip array

  // serach pattern given as input for Search
  public SearchWord(String patternString) {
    this.radix = 10000;

    // position of rightmost occurrence of c in the pattern
    right = new int[radix];
    for (int c = 0; c < radix; c++) right[c] = -1;
    for (int j = 0; j < patternString.length(); j++) right[patternString.charAt(
        j
      )] =
      j;
  }

  public static Hashtable<String, Integer> SearchWordAndFrequency(String word)
    throws IOException {
    Hashtable<String, Integer> fileList = new Hashtable<String, Integer>();
    File textFiles = new File("./text_pages");
    File[] textFileArray = textFiles.listFiles();
    int totalFiles = 0;

    for (int i = 0; i < textFileArray.length; i++) {
      String text = readFile(textFileArray[i].getPath());

      int frequency = wordSearch(text, word, textFileArray[i].getName());
      //Pattern p = Pattern.compile("::");
      //String[] file_name = p.split(text);
      if (frequency != 0) {
        fileList.put(textFileArray[i].getName(), frequency);
        //FileList.put(file_name[0], WordFrequency);
        totalFiles++;
      }
    }
    if (totalFiles > 0) {
      System.out.println(
        "\nWord \"" + word + "\" found in total " + totalFiles + " files"
      );
    } else {
      System.out.println(
        "\nWord \"" + word + "\" could not be found in any file"
      );
    }
    return fileList;
  }

  public static String readFile(String fileName) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(fileName));
    try {
      StringBuilder strb = new StringBuilder();
      String line = br.readLine();

      while (line != null) {
        strb.append(line);
        strb.append("\n");
        line = br.readLine();
      }
      return strb.toString();
    } finally {
      br.close();
    }
  }

  public static int search(String pattern, String text) {
    int patLength = pattern.length();
    int txtLength = text.length();
    int skip;
    for (int i = 0; i <= txtLength - patLength; i += skip) {
      skip = 0;
      for (int j = patLength - 1; j >= 0; j--) {
        if (pattern.charAt(j) != text.charAt(i + j)) {
          skip = Math.max(1, j - right[text.charAt(i + j)]);
          break;
        }
      }
      if (skip == 0) return i; // found
    }
    return txtLength; // not found
  }

  public static int wordSearch(String data, String word, String fileName) {
    int counter = 0;

    int offset = 0;
    SearchWord sw = new SearchWord(word);

    for (
      int location = 0;
      location <= data.length();
      location += offset + word.length()
    ) {
      offset = sw.search(word, data.substring(location));
      if ((offset + location) < data.length()) {
        counter++;
      }
    }
    return counter;
  }
  // return offset of first match; N if no match

}
