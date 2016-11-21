package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private ArrayList<String> goodWords;
    Random random;
    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        random = new Random();
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if(prefix.isEmpty()) {
            return words.get(random.nextInt(words.size()));
        }else {
            return binarySearchWord(0,words.size()-1,prefix);
        }
    }

    private String binarySearchWord(int begin,int end,String prefix) {
        int mid = (begin+end)/2;
        if(end > begin) {
            String temp = words.get(mid);

            if(temp.equals(prefix)){
                return null;
            }else if(temp.length()>(prefix.length()-1) && temp.substring(0,prefix.length()).equals(prefix)){
                return temp;
            }else if(temp.compareTo(prefix)<0){
                return binarySearchWord(mid+1,end,prefix);
            } else if(temp.compareTo(prefix)>0){
                return binarySearchWord(begin,mid-1,prefix);
            }
        }
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        if(prefix.isEmpty()) {
            random = new Random();;
            return words.get(random.nextInt(words.size()));
        }else {
            int length = prefix.length();
            String match = binarySearchWord(0,words.size()-1,prefix);
            if(match != null) {
                int size = match.length();
                goodWords = new ArrayList<>();
                while(match.substring(0,length).equals(prefix)){
                    if((length%2==0&&size%2==0)||(length%2!=0&&size%2!=2)) {
                        goodWords.add(match);
                    }
                    match = words.get(words.indexOf(match)+1);
                    size = match.length();
                }
                int roll = random.nextInt(goodWords.size());
                return goodWords.get(roll);
            }
            return null;
        }
    }
}
