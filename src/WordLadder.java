import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class WordLadder {

    public static boolean isEnglishWord(String word) {
        try {
            File bank = new File("words_alpha.txt") ;
            Scanner reader = new Scanner(bank) ;

            while (reader.hasNextLine()) {
                String temp = reader.nextLine() ;
                if (temp.equals(word)) {
                    return true ;
                }
            }
            reader.close() ;
        }
        catch(FileNotFoundException e) {
            System.out.println("File not found!") ;
        }
        return false ;
    }

    public static void getAllWords(List<String> bankStrings, int length) {
        try {
            File bank = new File("words_alpha.txt") ;
            Scanner reader = new Scanner(bank) ;

            while (reader.hasNextLine()) {
                String temp = reader.nextLine() ;
                if (temp.length() == length) {
                    bankStrings.add(temp) ;
                }
            }

            reader.close() ;
        }
        catch(FileNotFoundException e) {
            System.out.println("File not found!") ;
        }
    }

    public static boolean oneLetterDiff(String word1, String word2) {
        int result = 0 ;
        for (int i = 0 ; i < word1.length() ; i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                result += 1 ;
            }
        }
        if (result == 1) {
            return true ;
        }
        else {
            return false ;
        }
    }

    public static int countLetterDiff(String word1, String word2) {
        int result = 0 ;
        for (int i = 0 ; i < word1.length() ; i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                result += 1 ;
            }
        }
        return result ;
    }

    public static List<String> getAllPossibleWords(String word, List<String> bank) {
        List<String> result = new ArrayList<>() ;
        for(int i = 0 ; i < bank.size() ; i++) {
            if (oneLetterDiff(word, bank.get(i))) {
                result.add(bank.get(i)) ;
            }
        }
        return result ;
    }

    public static void GBFS(String start, String goal) {
        // set start time
        long startTime = System.currentTimeMillis() ;

        List<String> result = new ArrayList<>() ; // declare variable hasil akhir
        result.add(start) ;

        // list berisi seluruh String yang panjangnya sama
        List<String> bankStrings = new ArrayList<>();
        getAllWords(bankStrings, start.length()) ;

        // set berisi String yang telah diaktivasi
        Set<String> stringSet = new HashSet<>();
        stringSet.add(start) ;

        int wordCount = 0 ;

        while(true) {
            if (result.getLast().equals(goal)) {
                break ;
            }
            List<String> possibleWords = getAllPossibleWords(result.getLast(), bankStrings) ;
            int indexNext = -1;
            int countLetterDiff = 999;
            for (int i = 0 ; i < possibleWords.size() ; i++) {
                if (!result.contains(possibleWords.get(i))) {
                    int tempCount = countLetterDiff(possibleWords.get(i), goal) ;
                    if (tempCount <= countLetterDiff) {
                        countLetterDiff = tempCount ;
                        indexNext = i ;
                    }
                }
            }   
            if (indexNext == -1) {
                break ;
            }
            result.add(possibleWords.get(indexNext)) ;
            stringSet.add(possibleWords.get(indexNext)) ;
            wordCount += 1 ;
        }

        long endTime = System.currentTimeMillis() ;
        if (result.size() == 0 || !result.getLast().equals(goal)) {
            // result.clear();
            System.out.println("No path found!");
        }
        else {
            System.out.println(result);
        }
        System.out.printf("Node visited : %d \n" , wordCount);
        System.out.printf("Execution time : %d ms", (endTime - startTime), "\n");
    }

    public static void Astar(String start, String goal) {
        // set start time
        long startTime = System.currentTimeMillis() ;

        // Membuat antrian node
        Queue<List<String>> antrian = new PriorityQueue<>(new wordComparator(goal)) ;
        List<String> awal = new ArrayList<>() ;
        awal.add(start) ;
        antrian.add(awal) ;

        List<String> result = new ArrayList<>() ; // declare variable hasil akhir

        // list berisi seluruh String yang panjangnya sama
        List<String> bankStrings = new ArrayList<>();
        getAllWords(bankStrings, start.length()) ;

        // set berisi String yang telah diaktivasi
        Set<String> stringSet = new HashSet<>();
        stringSet.add(start) ;

        int wordCount = 0 ;

        while (!antrian.isEmpty()) {
            List<String> anak = antrian.poll() ;
            List<String> possibleWords = getAllPossibleWords(anak.getLast(), bankStrings) ;
            boolean checkResult = false ;

            for(int i = 0 ; i < possibleWords.size() ; i++) {
                if (!stringSet.contains(possibleWords.get(i))) {
                    if (possibleWords.get(i).equals(goal)) {
                        checkResult = true ;
                        result = anak ;
                        result.add(goal) ;
                        break ;
                    }
                    List<String> in = new ArrayList<>(anak) ; 
                    in.add(possibleWords.get(i)) ;
                    antrian.add(in) ;
                    wordCount += 1 ;
                    stringSet.add(possibleWords.get(i)) ;
                }
            }

            if (checkResult) {
                break ;
            }
        }
        long endTime = System.currentTimeMillis() ;
        if (result.size() == 0 || !result.getLast().equals(goal)) {
            // result.clear();
            System.out.println("No path found!");
        }
        else {
            System.out.println(result);
        }
        System.out.printf("Node visited : %d \n" , wordCount);
        System.out.printf("Execution time : %d ms", (endTime - startTime), "\n");
    }

    public static void UCS(String start, String goal) {
        // set start time
        long startTime = System.currentTimeMillis() ;

        // Membuat antrian node
        Queue<List<String>> antrian = new LinkedList<>() ;
        List<String> awal = new ArrayList<>() ;
        awal.add(start) ;
        antrian.add(awal) ;

        List<String> result = new ArrayList<>() ; // declare variable hasil akhir

        // list berisi seluruh String yang panjangnya sama
        List<String> bankStrings = new ArrayList<>();
        getAllWords(bankStrings, start.length()) ;

        // set berisi String yang telah diaktivasi
        Set<String> stringSet = new HashSet<>();
        stringSet.add(start) ;

        int wordCount = 0 ;

        while (!antrian.isEmpty()) {
            List<String> anak = antrian.poll() ;
            List<String> possibleWords = getAllPossibleWords(anak.getLast(), bankStrings) ;
            boolean checkResult = false ;

            for(int i = 0 ; i < possibleWords.size() ; i++) {
                if (!stringSet.contains(possibleWords.get(i))) {
                    if (possibleWords.get(i).equals(goal)) {
                        checkResult = true ;
                        result = anak ;
                        result.add(goal) ;
                        break ;
                    }
                    List<String> in = new ArrayList<>(anak) ; 
                    in.add(possibleWords.get(i)) ;
                    antrian.add(in) ;
                    stringSet.add(possibleWords.get(i)) ;
                    wordCount += 1 ;
                }
            }

            if (checkResult) {
                break ;
            }
        }
        long endTime = System.currentTimeMillis() ;
        if (result.size() == 0 || !result.getLast().equals(goal)) {
            // result.clear();
            System.out.println("No path found!");
        }
        else {
            System.out.println(result);
        }
        System.out.printf("Node visited : %d \n" , wordCount);
        System.out.printf("Execution time : %d ms", (endTime - startTime), "\n");
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Word Ladder Solver!");

        Scanner input = new Scanner(System.in) ;
        String start ;
        String end ;

        while (true) {
            System.out.print("Start word : ") ;
            start = input.next() ;
            start = start.toLowerCase() ;
            if (isEnglishWord(start)) {
                break ;
            }
            
            System.out.print("It is not a word! Try Again!\n") ;
        }
        while (true) {
            System.out.print("End word : ") ;
            end = input.next() ;
            end = end.toLowerCase() ;
            if (isEnglishWord(end)) {
                break ;
            }            
            System.out.print("It is not a word! Try Again!\n") ;
        }

        if (start.length() != end.length()) {
            System.out.println("Word's length doesn't match!") ;
            input.close();
            return ;
        }

        System.out.println("Choose which algorithm to use! \n1. UCS \n2. Greedy Best First Search \n3. A*") ;
        System.out.print("Input : ") ;
        String algo = input.next() ;
        while(true) {
            if (!algo.equals("1") && !algo.equals("2") && !algo.equals("3")) {
                System.out.println("Only the number! Try again!");
                System.out.print("Input : ");
                algo = input.next() ;
            }
            else {
                input.close();
                break ;
            }
        }

        if (start.equals(end)) {
            System.out.println(start);
            System.out.printf("Node visited : 0 \n");
            System.out.printf("Execution time : 0 ms \n");  
        }
        else {
            if(algo.equals("1")) {
                UCS(start, end) ;
            }
            else if (algo.equals("2")) {
                GBFS(start, end);
            }
            else {
                Astar(start, end) ;
            }
        }
    }
}

class wordComparator implements Comparator<List<String>> {
    private String goal ;

    public wordComparator(String goal) {
        this.goal = goal ;
    }
    public int compare(List<String> word1, List<String> word2) {
        // perhitungan cost heuristic h(n), node menuju goal
        int count1 = WordLadder.countLetterDiff(word1.getLast(), goal) ;
        int count2 = WordLadder.countLetterDiff(word2.getLast(), goal) ;
        // perhitungan cost g(n), cost dari start menuju node
        count1 += word1.size() ;
        count2 += word2.size() ;

        if (count1 < count2) {
            return -1 ;
        }
        else if (count1 > count2) {
            return 1 ;
        }
        else {
            return 0 ;
        }
    }
}