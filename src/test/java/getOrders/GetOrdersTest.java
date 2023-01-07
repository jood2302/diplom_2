package getOrders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import main.Credentials;
import main.User;
import main.clients.OrderClient;
import main.clients.UserClient;
import main.generators.UserGenerator;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrdersTest {
    private User user;

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
        statusCode = 200;
        expected = true;
    }


    @Test
    @DisplayName("Find order to authorized user")
    public void findOrderToAuthorizedUser() {
        userClient.loginUser(Credentials.from(user));
        Response response1 = orderClient.findOrder(accessToken);
        response1.then().statusCode(statusCode).and().body("success", equalTo(expected));
        userClient.deleteUser(accessToken);
    }
}