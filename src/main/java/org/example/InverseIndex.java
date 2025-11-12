package org.example;

import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.io.*;
import java.util.*;

// TODO: add error handling (try-w-resources?)

public class InverseIndex {
    HashMap<String, ArrayList<NotesDoc>> inverseIndexMap;
    int numOfDocs;

    public InverseIndex() throws IOException {
        this.inverseIndexMap = build();
    }

    private HashMap<String, ArrayList<NotesDoc>> build() throws IOException {
        // TODO: make path user-supplied rather than hardcoded
        // TODO: need to handle files that are not .txt
        File notesFolder = new File("path/to/meeting/notes/dir");
        File[] listOfFiles = notesFolder.listFiles();

        ArrayList<NotesDoc> allNotes = new ArrayList<>();
        ArrayList<String> terms = new ArrayList<>();

        // Get and tokenise the content of each file, then store in NotesDoc objects
        if  (listOfFiles != null) {
            this.numOfDocs = listOfFiles.length;
            for (File file : listOfFiles) {
                String currName = file.getName();
                String[] currContent = Tokenizer.tokenize(file);
                NotesDoc currNotesDoc = new NotesDoc(currName, "andye", currContent);
                allNotes.add(currNotesDoc);

                terms.addAll(List.of(currContent));
            }
        }

        // Remove stop words and get only remainder terms
        Set<String> uniqueTerms = new HashSet<>(removeStopWords(terms));

        // Build inverted index map
        HashMap<String, ArrayList<NotesDoc>> invertedIndex = new HashMap<>();
        for (String term : uniqueTerms) {
            ArrayList<NotesDoc> matchingDocs = new ArrayList<>();
            for (NotesDoc note : allNotes) {
                // System.out.println(FuzzySearch.partialRatio("which", "whihc"));
                if (note.content.stream().anyMatch(x -> x.equals(term))) {
                    matchingDocs.add(note);
                }
            }
            invertedIndex.put(term, matchingDocs);
        }

        return invertedIndex;
    }

    private ArrayList<String> removeStopWords(ArrayList<String> terms) throws IOException {
        // read stop words from file
        BufferedReader br = new BufferedReader(new FileReader("stop_words_english.txt"));
        List<String> stopWords = new ArrayList<>();

        String stopWord = br.readLine();
        while (stopWord != null) {
            stopWords.add(stopWord);
            stopWord = br.readLine();
        }
        br.close();

        // remove all stop words from tokens
        terms.removeAll(stopWords);

        return terms;
    }
}
