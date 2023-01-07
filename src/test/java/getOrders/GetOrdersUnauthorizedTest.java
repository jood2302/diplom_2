package getOrders;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import main.clients.OrderClient;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class GetOrdersUnauthorizedTest {
    private OrderClient orderClient;
    private int
            statusCodeError;
    private String
            errorMessage,
            nullAccessToken;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        nullAccessToken = "";
        statusCodeError = 401;
        errorMessage = "You should be authorised";
    }

    @Test
    @DisplayName("Find order to not authorized user")
    public void findOrderToNotAuthorizedUser() {
        Response response2 = orderClient.findOrder(nullAccessToken);
        response2.then().statusCode(statusCodeError).and().body("message", equalTo(errorMessage));
    }
}
