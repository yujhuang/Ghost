package com.google.engedu.ghost;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;


public class TrieNode {
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        int index = 0;
        int length = s.length();
        TrieNode node = this;
        for(;index < length;index++) {
            HashMap hash = node.children;
            String key = String.valueOf(s.charAt(index));
            if(hash.containsKey(key)) {
                node=(TrieNode)hash.get(key);
            }else {
                TrieNode nhash = new TrieNode();
                hash.put(key,nhash);
                node = nhash;       //check for key contained or not
                if(index == length-1)
                {
                    nhash.children.put(s,null);
                }
            }
        }
    }


    public boolean isWord(String s) {
        TrieNode node = this;
        HashMap hash;
        int length = s.length();
        int index = 0;
        for(;index < length; index++)  {
            hash = node.children;
            String key = String.valueOf(s.charAt(index));
            if(hash.containsKey(key)) {
                node = (TrieNode)hash.get(key);
            }else {
                return false;
            }
        }
        hash = node.children;
        if(hash.containsKey(s)) {
            return true;
        }
        return false;

    }


    public String getAnyWordStartingWith(String s) {
        TrieNode node = this;
        HashMap hash;
        int length = s.length();
        int index = 0;
        for (;index < length; index++) {
            hash = node.children;
            String key = String.valueOf(s.charAt(index));
            if(hash.containsKey(key)){
                node = (TrieNode)hash.get(key);
            }else {
                return null;
            }
        }
        String randomKey = null;
        while(node != null) {
            hash = node.children;
            Random random = new Random();
            ArrayList<String> keys = new ArrayList<String>(hash.keySet());
            randomKey = keys.get( random.nextInt(keys.size()) );
            node = (TrieNode)hash.get(randomKey);
        }
        return randomKey;
    }

    public String getGoodWordStartingWith(String s) {return null;}
}
