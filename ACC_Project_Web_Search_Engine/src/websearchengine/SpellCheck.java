package websearchengine;

import java.io.*;
import java.util.*;

public class SpellCheck {

    private static ArrayList<String> vocab = new ArrayList<>();

    private static void getVocab() throws IOException {
        String currentDir = System.getProperty("user.dir");
        File textFiles = new File(currentDir+ "/text_pages");

        File[] texts = textFiles.listFiles();

        StringBuilder lineThread = new StringBuilder();
        assert texts != null;
        for (File text : texts) {
            BufferedReader br = new BufferedReader(new FileReader(text));
            String str;
            while ((str = br.readLine()) != null) {
                lineThread.append(str);
            }
            br.close();
        }
        String fullText = lineThread.toString();
        StringTokenizer tokenizer = new StringTokenizer(fullText, "0123456789 ,`*$|~(){}_@><=+[]\\?;/&#-.!:\"'\n\t\r");
        while (tokenizer.hasMoreTokens()) {
            String tk = tokenizer.nextToken().toLowerCase(Locale.ROOT);
            if (!vocab.contains(tk)) {
                vocab.add(tk);
            }
        }
    }

    public static String[] getOtherWords(String query) throws IOException {
        getVocab();
        HashMap<String, Integer> hashMap = new HashMap<>();
        String[] otherWords = new String[10];
        for (String word : vocab) {
            int editDistance = wordEditDistance(query, word);
            hashMap.put(word, editDistance);
        }
        Map<String, Integer> map = sortByValue(hashMap);

        int rank = 0;
        for (Map.Entry<String, Integer> en : map.entrySet()) {
            if (en.getValue() != 0) {
                otherWords[rank] = en.getKey();
                rank++;
                if (rank == 10){ break; }
            }
        }
        return otherWords;
    }

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> map) {
        List<Map.Entry<String, Integer> > list = new LinkedList<>(map.entrySet());

        list.sort(Map.Entry.comparingByValue());

        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public static int wordEditDistance(String w1, String w2) {
        int word1Length = w1.length();
        int word2Lenghth = w2.length();
        int[][] dataArray = new int[word1Length + 1][word2Lenghth + 1];

        for (int i = 0; i <= word1Length; i++) {
            dataArray[i][0] = i;
        }
        for (int j = 0; j <= word2Lenghth; j++) {
            dataArray[0][j] = j;
        }

        for (int i = 0; i < word1Length; i++) {
            char char1 = w1.charAt(i);
            for (int j = 0; j < word2Lenghth; j++) {
                char char2 = w2.charAt(j);

                if (char1 == char2) {
                    dataArray[i + 1][j + 1] = dataArray[i][j];
                } else {
                    int replaceValue = dataArray[i][j] + 1;
                    int insertValue = dataArray[i][j + 1] + 1;
                    int deleteValue = dataArray[i + 1][j] + 1;

                    int min = Math.min(replaceValue, insertValue);
                    min = Math.min(deleteValue, min);
                    dataArray[i + 1][j + 1] = min;
                }
            }
        }
        return dataArray[word1Length][word2Lenghth];
    }

    public static void main(String[] args) throws IOException{
        String[] list = getOtherWords("a");
        System.out.println(list.length+""+ list[0]);
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
}
