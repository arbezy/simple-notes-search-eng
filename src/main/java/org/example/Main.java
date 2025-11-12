package org.example;

import java.io.*;

// TODO: save the inverse index so it is only rebuilt on command OR detect whe a new file is added then update the index
// TODO: create a simple stemmer (should also clean punctuation) INPROG
// TODO: write some tests LOL
// TODO: search through titles of socuments too, as well as contents
// TODO: find a way to look for mispelled word too
//      - made an attempt using fuzzysearch but it didn't really work
// TODO: fix bug where null pointer crashes app if one fo multiple search terms doesnt exist in index

public class Main {
    private static Boolean quitRegistered = false;

    public static void main(String[] args) throws IOException {
        InverseIndex invIdx = new InverseIndex();
        System.out.println("inverse index built");

        while (!quitRegistered) {
            Searcher.search(invIdx);
        }
    }

    public static void requestQuit() {
        quitRegistered = true;
    }
}
