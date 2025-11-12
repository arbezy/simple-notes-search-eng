package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotesDoc {
    String title;
    String author;
    List<String> content;
    HashMap<String, Integer> termCount;

    public NotesDoc(String title, String author, String[] content) {
        this.title = title;
        this.author = author;
        this.content = preprocess(content);
        this.termCount = getTermCount();
    }

    private HashMap<String, Integer> getTermCount() {
        HashMap<String, Integer> wordToCount = new HashMap<>();
        content.stream().parallel()
                .map(x -> wordToCount.put(x, wordToCount.getOrDefault(x, 0) + 1))
                .toArray();
        return wordToCount;
    }

    private List<String> preprocess(String[] contentTokens) {
        List<String> preprocessedTokens = new ArrayList<>(List.of(contentTokens));
        // TODO: remove punctutation
        // TODO: stemming

        return preprocessedTokens;
    }
}
