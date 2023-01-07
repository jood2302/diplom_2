package main.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import main.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {

    public static final String
            ORDER = "/api/orders",
            INGREDIENTS = "/api/ingredients";

    @Step("Get ingredients")
    public Response getIngredients() {
        Response response =
                given()
                        .spec(getSpec())
                        .when()
                        .get(INGREDIENTS);
        return response;
    }

    @Step("Create new order")
    public Response createNewOrder(String accessToken, Order ingredients) {
        Response response =
                given()
                        .spec(getSpec())
                        .header("Authorization", accessToken)
                        .body(ingredients)
                        .when()
                        .post(ORDER);
        return response;
    }

    @Step("Get order")
    public Response findOrder(String accessToken) {
        Response response =
                given()
                        .header("Authorization", accessToken)
                        .spec(getSpec())
                        .when()
                        .get(ORDER);
        return response;
    }
}