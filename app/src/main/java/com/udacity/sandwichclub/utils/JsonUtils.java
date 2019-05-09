package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String NAME = "name";
    private static final String MAIN_NAME = "mainName";
    private static final String ALSO_KNOWN_AS = "alsoKnownAs";

    private static final String PLACE_OF_ORIGIN = "placeOfOrigin";
    private static final String DESCRIPTION = "description";
    private static final String IMAGE = "image";
    private static final String INGREDIENTS = "ingredients";


    public static Sandwich parseSandwichJson(String json) {
        Sandwich sandwich = new Sandwich();
        try {
            JSONObject sandwichJsonObject = new JSONObject(json);

            if (sandwichJsonObject.has(NAME)) {
                JSONObject nameJsonObject = sandwichJsonObject.getJSONObject(NAME);
                if (nameJsonObject.has(MAIN_NAME)) {
                    sandwich.setMainName(nameJsonObject.getString(MAIN_NAME));
                }
                if (nameJsonObject.has(ALSO_KNOWN_AS)) {
                    JSONArray alsoKnownAsJsonArray = nameJsonObject.getJSONArray(ALSO_KNOWN_AS);
                    sandwich.setAlsoKnownAs(convertJsonArrayToStringList(alsoKnownAsJsonArray));
                }
            }

            if (sandwichJsonObject.has(PLACE_OF_ORIGIN)) {
                sandwich.setPlaceOfOrigin(sandwichJsonObject.getString(PLACE_OF_ORIGIN));
            }
            if (sandwichJsonObject.has(DESCRIPTION)) {
                sandwich.setDescription(sandwichJsonObject.getString(DESCRIPTION));
            }
            if (sandwichJsonObject.has(IMAGE)) {
                sandwich.setImage(sandwichJsonObject.getString(IMAGE));
            }
            if (sandwichJsonObject.has(INGREDIENTS)) {
                JSONArray ingredientsJsonArray = sandwichJsonObject.getJSONArray(INGREDIENTS);
                sandwich.setIngredients(convertJsonArrayToStringList(ingredientsJsonArray));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return sandwich;
    }

    private static List<String> convertJsonArrayToStringList(JSONArray jsonArray) {
        ArrayList<String> list = new ArrayList<>();
        if (jsonArray != null) {
            int len = jsonArray.length();
            for (int i = 0; i < len; i++) {
                try {
                    String currentValue = jsonArray.getString(i);
                    if (currentValue != null) {
                        list.add(currentValue);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /*{"name":
        {"mainName":"Ham and cheese sandwich","alsoKnownAs":["Some info about"]},
        "placeOfOrigin":"",
            "description":"A ham and cheese sandwich is a common type of sandwich. It is made by putting cheese and sliced ham between two slices of bread. The bread is sometimes buttered and/or toasted. Vegetables            like lettuce, tomato, onion or pickle slices can also be included. Various kinds of mustard and mayonnaise are also common.",
            "image":"https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Grilled_ham_and_cheese_014.JPG/800px-Grilled_ham_and_cheese_014.JPG",
            "ingredients":["Sliced bread","Cheese","Ham"]
    }*/
}
