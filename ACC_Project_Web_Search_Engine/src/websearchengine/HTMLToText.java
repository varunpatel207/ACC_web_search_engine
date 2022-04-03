package websearchengine;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class HTMLToText {

  public static void StaticHTMLtoTextConverter() throws Exception {
    File myfile = new File("url_pages_fixed");
    String[] fileList = myfile.list();
    for (int i = 0; i < fileList.length; i++) {
      ConvertHtmlToText(fileList[i], "fixed"); //take html files from url_pages_fixed and convert them to text
    }
  }

  public static void DynamicHTMLtoTextConverter(ArrayList<String> linkList)
    throws Exception {
    System.out.println("Links Crawled: \n");
    int inc = 0;
    for (String s : linkList) {
      //System.out.println("Link1: "+s);
      inc++;
      Document docNewLink = Jsoup.connect(s).get();

      String html = docNewLink.html();
      String htmlOutputPath = "url_pages_crawled";
      File htmlOutputFolder = new File(htmlOutputPath);
      String regex = "[a-zA-Z0-9]+";
      Pattern p2 = Pattern.compile(regex);
      Matcher m2 = p2.matcher(s);
      StringBuffer sb = new StringBuffer();
      while (m2.find()) {
        sb.append(m2.group(0));
      }

      String linkAdress = sb.substring(0);
      System.out.println("Link: " + linkAdress);

      PrintWriter out = new PrintWriter(
        htmlOutputPath + "\\" + linkAdress + ".html"
      );
      out.println(html);
      out.close();

      if (inc == 20) {
        break;
      }
    }
    File myfile = new File("url_pages_crawled");
    String[] fileList = myfile.list();
    for (int i = 0; i < fileList.length; i++) {
      ConvertHtmlToText(fileList[i], "crawled"); //take html files from W3C Web Pages and convert them to text
    }
  }

  public static void ConvertHtmlToText(String file, String type)
    throws Exception {
    //System.out.println("File Name: "+file);
    String folderToFetch;
    if (type.equals("fixed")) {
      folderToFetch = "url_pages_fixed";
    } else {
      folderToFetch = "url_pages_crawled";
    }
    File f1 = new File(folderToFetch + "\\" + file);
    //Parse the file using JSoup
    Document doc = Jsoup.parse(f1, "UTF-8");
    //Convert the file to text
    String str = doc.text();
    PrintWriter pw = new PrintWriter(
      "text_pages\\" + file.replaceAll(".html", ".txt")
    );
    pw.println(str);
    pw.close();
  }
}
