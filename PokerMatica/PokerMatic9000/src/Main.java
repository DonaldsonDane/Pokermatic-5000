// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.


import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.Arrays;


public class Main {

    private String[] usersCards;

    public enum handResults {
        HIGH_CARD,
        PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        STRAIGHT,
        FLUSH,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        STRAIGHT_FLUSH,
        ROYAL_FLUSH
    }


    public static void main(String[] args) {


        System.out.println("Welcome To The PokerMatic 9000!");
        System.out.println("================================================");

        System.out.println("Please enter your five cards, seperated by a comma");
        GetCards();

    }


    public static void GetCards() {
        int n;
        Scanner sc = new Scanner(System.in);


        n = 1;
//creates an array in the memory of length 10
        String[] array = new String[10];

        for (int i = 0; i < n; i++) {
//reading array elements from the user
            String card = sc.nextLine();

           ValidateCard(card);


        }

    }


    public static void ValidateCard(String card) {
        String[] userInput = card.split(",");
        boolean validationSuccess;
        int cardSuccess = 0;

        try {
            // Read JSON file content
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/carddata.json")));

            // Parse JSON content
            JSONObject jsonObject = new JSONObject(jsonContent);
            JSONArray cardsArray = jsonObject.getJSONArray("cards");

            for (String tempInput : userInput) {
                // Compare user input with each card ID
                for (int j = 0; j < cardsArray.length(); j++) {
                    JSONObject cardObject = cardsArray.getJSONObject(j);
                    String cardId = cardObject.getString("id");

                    if (tempInput.equals(cardId)) {

                        cardSuccess++;

                    }



                }



            }
            if(cardSuccess != 5){

               ThrowError();


            }else{
                WorkOutHand(userInput);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void ThrowError(){
        System.out.println("You entered an invalid card. Please try again");
        System.out.println("Please enter your five cards, seperated by a comma");
        GetCards();
    }

    public static void WorkOutHand(String[] input){
        try {
            // Read JSON file content
            String jsonContent = new String(Files.readAllBytes(Paths.get("src/carddata.json")));

            // Parse JSON content
            JSONObject jsonObject = new JSONObject(jsonContent);
            JSONArray ranksArray = jsonObject.getJSONArray("ranks");

            for (int i = 0; i < ranksArray.length(); i++) {
                JSONObject rankObject = ranksArray.getJSONObject(i);
                String rank = rankObject.getString("rank");
                JSONArray cardsRequiredArray = rankObject.getJSONArray("cards_required");

                // Check if input matches any cards_required array
                for (int j = 0; j < cardsRequiredArray.length(); j++) {
                    String[] requiredCards = cardsRequiredArray.getString(j).split(",");
                    boolean match = new HashSet<>(Arrays.asList(requiredCards)).containsAll(Arrays.asList(input));

                    if (match) {
                        System.out.println("Hand: " + rank);
                        return;  // Stop further checking once a match is found
                    }
                }
            }

            // If no match is found
            System.out.println("No matching hand found.");
            System.out.println("Try Again...");
            System.out.println("Please enter your five cards, seperated by a comma");
            GetCards();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
