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
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest {
    private User user;
    private Order order;
    private UserClient userClient;
    private OrderClient orderClient;
    private int
            statusCode;
    private boolean
            expected;
    private String
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
        expected = true;
    }

    @Test
    @DisplayName("Create valid Order for authorized user")
    public void createValidOrder() {
        Response response2= orderClient.createNewOrder(accessToken, order);
        response2.then().assertThat().statusCode(statusCode)
                .and().body("success", equalTo(expected));
    }

    @After
    public void delete() {
        userClient.deleteUser(accessToken);
    }
}