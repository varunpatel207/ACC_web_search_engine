package websearchengine;

import java.io.File;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;

public class Entry {

  private static Scanner sc = new Scanner(System.in);

  public static void main(String args[]) throws Exception {
    // initial delete
    deleteAllFiles("./text_pages");
    deleteAllFiles("./url_pages_crawled");

    System.out.println("Web Search Engine\n\n");

    System.out.println(
      "How do you want to search? By word(w) or By URL(u)? \n"
    );
    String userSearchChoice = sc.next();
    //System.out.println("User Selection: "+userSearchChoice);
    if (userSearchChoice.equals("u")) {
      System.out.println("Enter the URL to Search: \n");
      String inputURL = sc.next();
      String formedURL = "https://" + inputURL + "/";

      // remove whitespaces
      inputURL = inputURL.trim();

      //URL Validation through regex
      boolean isURL = RegexMatch.isValidURL(inputURL);
      //Crawler and HTML to Text Converion
      Crawler c = new Crawler();
      c.getLinks(formedURL);
    } else {
      //HTML to Text Converion
      HTMLToText htmlToText = new HTMLToText();
      HTMLToText.StaticHTMLtoTextConverter();
    }
    while (true) {
      System.out.println("\n\nEnter 0 to exit or Enter any number to continue");

      if (sc.nextInt() == 0) {
        System.out.println("Thank you. \n");
        break;
      }

      System.out.println("Enter the word to Search: ");
      String wordToSearch = sc.next();

      //Edit Distance

      //BoyerMoore
      SearchWord sw = new SearchWord(wordToSearch);
      Hashtable<String, Integer> FileList = sw.SearchWordAndFrequency(
        wordToSearch
      );

      if (FileList.isEmpty()) {
        System.out.println("No files found, here are some suggestions: ");
        String[] list = SpellCheck.getOtherWords(wordToSearch);
        if(list.length > 0) {
            if(list[0] != null) {
                for(String item: list){
                    System.out.println(item);
                }
            }else {
                System.out.println("No suggestions found!");
            }
        } else {
            System.out.println("No suggestions found!");
        }
      }
      //Sorting
      else {
        System.out.println(
          "How do you want the result? Sort By Rank(r) or Alphabetical Order(a): "
        );
        String userSortInput = sc.next();
        SortPages sortPages = new SortPages();
        if (userSortInput.equals("r")) {
          sortPages.rankWebPages(FileList, FileList.size());
        } else {
          Set<String> keys = FileList.keySet();
          String[] pages = new String[FileList.size()];
          int i = 0;
          for (String key : keys) {
            pages[i] = key;
            i++;
          }
          sortPages.quicksort(pages);
        }

        //delete all files crawled and all converted files
        deleteAllFiles("./text_pages");
        deleteAllFiles("./url_pages_crawled");

        System.out.println("Thank You");
        break;
      }
    }
   
  }

  // deletes all files created in provided path
  private static void deleteAllFiles(String filePath) {
    File files = new File(filePath);
    File[] ArrayofFiles = files.listFiles();

    for (int i = 0; i < ArrayofFiles.length; i++) {
      ArrayofFiles[i].delete();
    }
  }
}
