package org.example;

// NOTE: could really swap out Pair from Entry
import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Searcher {
    public static void search(InverseIndex invIdx) throws IOException {
        System.out.println("input a search term, or press q to quit:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String searchTerm = br.readLine();
        String[] searchTermTokens = searchTerm.split(" ");

        if (Objects.equals(searchTermTokens[0], "q")) {
            Main.requestQuit();
        } else if (searchTermTokens.length > 1) {
            // Multiple Search Terms
            HashMap<String, Double> combinedTermScores = new HashMap<>();
            for (String searchTermToken : searchTermTokens) {
                if (invIdx.inverseIndexMap.get(searchTermToken) != null) {
                    // Get scores using the current term from the search terms
                    List<Pair<Double, String>> tokenRanking = rankMatchingDocs(
                            searchTermToken,
                            invIdx.inverseIndexMap.get(searchTermToken),
                            invIdx.numOfDocs
                    );
                    // Update map with score totals for the current term
                    for (Pair<Double, String> pair : tokenRanking) {
                        if (combinedTermScores.containsKey(pair.getValue1())) {
                            combinedTermScores.put(pair.getValue1(), combinedTermScores.get(pair.getValue1()));
                        } else {
                            combinedTermScores.put(pair.getValue1(), pair.getValue0());
                        }
                    }
                }
            }

            List<Pair<Double, String>> docRanking = new ArrayList<>();
            combinedTermScores.forEach((k, v) -> {
                docRanking.add(new Pair<>(v, k));
            });

            System.out.println("\ncombined term scores (in order of relevance):");
            docRanking.stream()
                    .sorted(Collections.reverseOrder())
                    .map(Pair::getValue1)
                    .forEach(System.out::println);
            System.out.println();

        } else {
            // Single Search Term
            if (!invIdx.inverseIndexMap.containsKey(searchTerm)) {
                System.out.println("no matching term found");
            } else {
                System.out.println("term found in files (in order of relevance):");
                rankMatchingDocs(searchTerm, invIdx.inverseIndexMap.get(searchTerm), invIdx.numOfDocs).stream()
                        .map(Pair::getValue1)
                        .forEach(System.out::println);
                System.out.println();
            }
        }
    }

    public static List<Pair<Double, String>> rankMatchingDocs(String searchTerm, ArrayList<NotesDoc> matchingDocs, int numOfDocs) {
        List<Pair<Double, String>> scoredDocs = new ArrayList<>();
        for (var doc : matchingDocs) {
            double termFrequency = (double) doc.termCount.get(searchTerm) / doc.content.size();
            double inverseDocumentFrequency = (1 + Math.log((double) numOfDocs / matchingDocs.size()));
            double tfIdfValue = termFrequency * inverseDocumentFrequency;

            Pair<Double, String> tfIdfPair = new Pair<>(tfIdfValue, doc.title);
            scoredDocs.add(tfIdfPair);
        }

        return scoredDocs.stream()
                .sorted(Collections.reverseOrder())
                .toList();
    }
}
