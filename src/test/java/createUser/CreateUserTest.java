package createUser;

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


public class CreateUserTest {

    private User user;
    private UserClient userClient;
    private int
            statusCode,
            statusCodeError;
    private boolean expected;
    private String
            userExistErrorMessage,
            notEnoughDataErrorMessage;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefault();
        statusCode = 200;
        statusCodeError = 403;
        expected = true;
        userExistErrorMessage = "User already exists";
        notEnoughDataErrorMessage = "Email, password and name are required fields";
    }

    @Test
    @DisplayName("Check post - creating user")
    public void checkPostCreatingUser() {
        user = UserGenerator.getDefault();
        Response response = userClient.createUser(user);
        response.then().assertThat().statusCode(statusCode)
                .and().body("success", equalTo(expected));
    }

    @Test
    @DisplayName("Check post- creating identical user")
    public void checkPostCreatingIdenticalUser() {
        userClient.createUser(user);
        Response response1 = userClient.createUser(user);
        response1.then().assertThat().statusCode(statusCodeError)
                .and().assertThat().body("message", equalTo(userExistErrorMessage));
    }

    @Test
    @DisplayName("Check post- creating user without Name")
    public void checkPostCreatingUserWithoutName() {
        user = UserGenerator.getWithoutName();
        Response response = userClient.createUser(user);
        response.then().assertThat().statusCode(statusCodeError)
                .and()
                .assertThat().body("message", equalTo(notEnoughDataErrorMessage));
    }
    @Test
    @DisplayName("Check post- creating user without Email")
    public void checkPostCreatingUserWithoutEmail() {
        user = UserGenerator.getWithoutEmail();
        Response response = userClient.createUser(user);
        response.then().assertThat().statusCode(statusCodeError)
                .and()
                .assertThat().body("message", equalTo(notEnoughDataErrorMessage));
    }

    @Test
    @DisplayName("Check post- creating user without Password")
    public void checkPostCreatingUserWithoutPassword() {
        user = UserGenerator.getWithoutPassword();
        Response response = userClient.createUser(user);
        response.then().assertThat().statusCode(statusCodeError)
                .and()
                .assertThat().body("message", equalTo(notEnoughDataErrorMessage));
    }


    @After
    public void delete() {
        Response response = userClient.loginUser(Credentials.from(user));
        String accessToken = response.then().extract().path("accessToken");
        userClient.deleteUser(accessToken);
    }
}