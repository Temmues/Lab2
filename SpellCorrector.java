package spell;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class SpellCorrector implements ISpellCorrector {
    
    //private trie
    private Trie wordPool;
    private HashSet<String> corrections = null;
    FileInputStream inputStream = null;

    public void clear()
    {
        wordPool.clear();
    }
    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        wordPool = new Trie();
        corrections = new HashSet<String>();
        try {
            File file = new File(dictionaryFileName);
            inputStream = new FileInputStream(file);
        } catch (Exception e) {
            System.err.println("File Error");
        }
        BufferedInputStream buff = new BufferedInputStream(inputStream);
        Scanner inputDict = new Scanner(buff); //drop file into scanner
        while(inputDict.hasNext())
        {
            String current = inputDict.next();
            if(!current.isEmpty())
            {
                wordPool.add(current);
            }
        }
        inputDict.close();
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        String fixedWord = inputWord.toLowerCase();
        INode returnedNode = wordPool.find(fixedWord);
        String returnString = null;
        if(returnedNode == null)
        {
            returnString = returnLikelyMatch(fixedWord);
        }
        else
        {
            returnString = fixedWord;
        }
        return returnString;
    }
    public String returnLikelyMatch(String inputWord)
    {
        INode n = null;
        HashSet<String> secondRound = new HashSet<String>();
        String initial = inputWord;
        deletion(inputWord);
        transposition(inputWord);
        alteration(inputWord);
        insertion(inputWord);
        List<String> ValidWords = new ArrayList<String>();
        for (String x: corrections) // 1 distance away
        {
            INode check = wordPool.find(x);
            secondRound.add(x);
            if (check != null)
            {
                ValidWords.add(x);
            }
        }
        int frequency = 0;
        for (String y: ValidWords)
        {
            if (wordPool.find(y).getValue() > frequency)
            {
                initial = y;
                frequency = wordPool.find(y).getValue();
            }
            else if (wordPool.find(y).getValue() == frequency)
            {
                if(initial.compareTo(y) > 0)
                {
                    initial = y;
                }
            }
        }
        if(ValidWords.isEmpty()) {
            for(String x : secondRound)
            {
                deletion(x);
                transposition(x);
                alteration(x);
                insertion(x);
            }
            for (String x : corrections) // 1 distance away
            {
                INode check = wordPool.find(x);
                if (check != null) {
                    ValidWords.add(x);
                }
            }
            for (String y : ValidWords) {
                if (wordPool.find(y).getValue() > frequency) {
                    initial = y;
                    frequency = wordPool.find(y).getValue();
                } else if (wordPool.find(y).getValue() == frequency) {
                    if (initial.compareTo(y) > 0) {
                        initial = y;
                    }
                }
            }
        }
        if(ValidWords.isEmpty())
        {
            return null;
        }
        return initial;
    }
    public void deletion(String inputWord)
    {
        if(inputWord.length() != 1) {
            for (int i = 0; i < inputWord.length(); i++) {
                StringBuilder choiceMaker = new StringBuilder(inputWord);
                choiceMaker.deleteCharAt(i);
                corrections.add(choiceMaker.toString());
            }
        }
    }
    public void transposition(String inputWord)
    {
        for (int i = 0; i < inputWord.length(); i++)
        {
            StringBuilder choiceMaker = new StringBuilder(inputWord);
            if (i < inputWord.length() - 1)// make sure we do not stray
            {
                String newString = inputWord;
                char first = choiceMaker.charAt(i);
                char second = choiceMaker.charAt(i+1);
                choiceMaker.deleteCharAt(i);
                choiceMaker.insert(i+1,first);
                corrections.add(choiceMaker.toString());
            }
        }
    }

    public void insertion(String inputWord)
    {
        final int ALPHABET = 26;
        for(int i = 0; i <= inputWord.length(); i++)
        {
            for (int j = 0; j < ALPHABET; j++)
            {
                StringBuilder choiceMaker = new StringBuilder(inputWord);
                int ascii = 'a' + j;
                char newLetter = (char) ascii;
                choiceMaker.insert(i,newLetter);
                //System.out.println(choiceMaker.toString());
                corrections.add(choiceMaker.toString());
            }
        }
    }
    public void alteration(String inputWord)
    {
        final int ALPHABET = 26;
        for(int i = 0; i < inputWord.length(); i++)
        {
            StringBuilder choiceMaker = new StringBuilder(inputWord);
            char currentLetter = choiceMaker.charAt(i);
            for (int j = 0; j < ALPHABET; j++)
            {
                int ascii = 'a' + j;
                char newLetter = (char) ascii;
                if (newLetter != currentLetter)
                {
                    choiceMaker.setCharAt(i,newLetter);
                    corrections.add(choiceMaker.toString());
                }
            }
        }
    }

    public HashSet<String> getCorrections() {
        return corrections;
    }
}
