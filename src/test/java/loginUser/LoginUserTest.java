package loginUser;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import main.Credentials;
import main.User;
import main.clients.UserClient;
import main.generators.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest {
    private User user;
    private UserClient userClient;
    private int
            statusCode,
            statusCodeError;
    private boolean
            expected,
            expectedError;
    private String
            errorMessage,
            accessToken,
            newPassword,
            newEmail;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefault();
        Response response = userClient.createUser(user);
        accessToken = response.then().extract().path("accessToken");
        statusCode = 200;
        statusCodeError = 401;
        expected = true;
        expectedError = false;
        errorMessage = "email or password are incorrect";
        newPassword = "a1";
        newEmail = "a2";

    }
    @Test
    @DisplayName("Authorization user")
    public void loginUser() {
        Response response1 = userClient.loginUser(Credentials.from(user));
        response1.then().assertThat().statusCode(statusCode).and().body("success", equalTo(expected));
    }

    @Test
    @DisplayName("Authorization with invalid password")
    public  void authorizationWithInvalidPassword(){
        User user =  UserGenerator.authorization();
        user.setPassword(newPassword);
        Response response1 = userClient.loginUser(Credentials.from(user));
        response1.then().assertThat().statusCode(statusCodeError).and().body("message", equalTo(errorMessage));
    }
    @Test
    @DisplayName("Authorization with invalid email")
    public void  authorizationWithInvalidEmail(){
        User user =  UserGenerator.authorization();
        user.setEmail(newEmail);
        Response response1 = userClient.loginUser(Credentials.from(user));
        response1.then().assertThat().statusCode(statusCodeError).and().body("message", equalTo(errorMessage));
    }

    @Test
    @DisplayName("Authorization with invalid password and email")
    public void  authorizationWithInvalidLogin(){
        User user =  UserGenerator.authorization();
        user.setEmail(newEmail);
        user.setPassword(newPassword);
        Response response1 = userClient.loginUser(Credentials.from(user));
        response1.then().assertThat().statusCode(statusCodeError).and().body("message", equalTo(errorMessage));
    }


    @After
    public void delete() {
        userClient.deleteUser(accessToken);
    }
}