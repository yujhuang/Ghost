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
            return binarySearchWord(prefix,0,words.size()-1);
        }
    }

    private String binarySearchWord(String prefix,int begin,int end) {
        int mid = (begin+end)/2;
        if(end > begin) {
            String temp = words.get(mid);

            if(temp.equals(prefix)){
                return null;
            }
            else if(temp.length()>(prefix.length()-1) && temp.substring(0,prefix.length()).equals(prefix)){
                return temp;
            }
            else if(temp.compareTo(prefix)<0){
                return binarySearchWord(prefix,mid+1,end);
            }
            else if(temp.compareTo(prefix)>0){
                return binarySearchWord(prefix,begin,mid-1);
            }
        }
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        if(prefix.isEmpty()) {
            random = new Random();
            int n = random.nextInt(words.size());
            return words.get(n);
        }else {
            int len = prefix.length(),size;
            goodWords = new ArrayList<>();
            String result = binarySearchWord(prefix,0,words.size()-1);
            if(result == null) {
                return null;
            }else {
                while(result.substring(0,len).equals(prefix)){
                    size = result.length();
                    if((len%2==0&&size%2==0)||(len%2!=0&&size%2!=2)) {
                        goodWords.add(result);
                    }
                    result = words.get(words.indexOf(result)+1);
                }
                return goodWords.get(goodWords.size()-1);
            }
        }
    }
}
