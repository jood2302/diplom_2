package createOrder;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import main.Order;
import main.User;
import main.clients.OrderClient;
import main.clients.UserClient;
import main.generators.OrderGenerator;
import main.generators.UserGenerator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateInvalidOrderTest {
    private User user;
    private Order order;
    private UserClient userClient;
    private OrderClient orderClient;
    private int
            statusCode,
            invalidIngredientStatusCode,
            nullIngredientStatusCode;
    private String
            nullIngredientErrorMessage,
            accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        user = UserGenerator.getDefault();
        Response response = userClient.createUser(user);
        accessToken = response.then().extract().path("accessToken");
        Response response1 = orderClient.getIngredients();
        List<String> jsonResponse = response1.then().extract().body().jsonPath().getList("data._id");
        order = OrderGenerator.getDefault(jsonResponse);
        statusCode = 200;
        invalidIngredientStatusCode = 500;
        nullIngredientStatusCode = 400;
        nullIngredientErrorMessage = "Ingredient ids must be provided";
    }

    @Test
    @DisplayName("Create Order for not authorized user")
    public void createOrderForNotAuthorizedUser() {
        String nullAccessToken = "";
        Response response10 = orderClient.createNewOrder(nullAccessToken, order);
        int statusCodeResponse = response10.then().extract().statusCode();
        Assert.assertEquals("Error - order can't be created for not authorized user! ", statusCodeResponse, statusCode);
    }

    @Test
    @DisplayName("Post invalid ingredients")
    public void createOrderWithInvalidIngredients() {
        order = OrderGenerator.getWithInvalidIngredients();
        Response response2 = orderClient.createNewOrder(accessToken, order);
        response2.then().assertThat().statusCode(invalidIngredientStatusCode);
    }

    @Test
    @DisplayName("Post without ingredients")
    public void createOrderWithoutIngredients() {
        order.ingredients.clear();
        Response response2 = orderClient.createNewOrder(accessToken, order);
        response2.then().assertThat().statusCode(nullIngredientStatusCode)
                .and().body("message", equalTo(nullIngredientErrorMessage));
    }


    @After
    public void delete() {
        userClient.deleteUser(accessToken);
    }
}