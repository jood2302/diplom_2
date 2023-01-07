package main.generators;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;
import main.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderGenerator {
    private static Faker faker = new Faker();

    public static List<String> ingredients = new ArrayList<>();

    public  static List<String> getIngredients(List<String> jsonResponse) {
        int randomInt = faker.number().numberBetween(0, jsonResponse.size());

        for (int i = 0; i<randomInt; i++){
            ingredients.add(jsonResponse.get(faker.number().numberBetween(0, jsonResponse.size())));
        }
        return ingredients;
    };

    public  static List<String> getInvalidIngredients(){
        ingredients.add(faker.bothify("?#####?123qwe"));
        return ingredients;
    };
    @Step("get order valid date")
    public static Order getDefault(List<String> jsonResponse){
        return new Order(getIngredients(jsonResponse));
    }

    @Step("get order invalid date")
    public static Order getWithInvalidIngredients(){
        return new Order(getInvalidIngredients());
    }
}