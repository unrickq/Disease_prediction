package com.example.diseaseprediction.tokenize;


/*
 * Define a 5-word/tag window object to capture the context surrounding a word
 */
public class FWObject
{
    public String[] context;

    public FWObject(boolean check)
    {
        context = new String[10];
        if (check == true) {
            for (int i = 0; i < 10; i += 2) {
                context[i] = "<W>";
                context[i + 1] = "<T>";
            }
        }
    }
}
