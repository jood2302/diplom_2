package changeUserData;

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

public class ChangeUserDataTest {

    private User user;
    private UserClient userClient;
    private Response response;
    private int
            statusCode,
            statusCodeLoginError,
            statusCodeEmailError;
    private boolean
            expected,
            expectedError;
    private String
            loginErrorMessage,
            emailErrorMessage,
            accessToken,
            newName = "2302leo",
            newEmail = "2302leo@yandex.ru",
            newPassword = "0123456789";

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefault();
        response = userClient.createUser(user);
        accessToken = response.then().extract().path("accessToken");
        statusCode = 200;
        statusCodeLoginError = 401;
        statusCodeEmailError = 403;
        expected = true;
        expectedError = false;
        loginErrorMessage = "You should be authorised";
        emailErrorMessage = "User with such email already exists";

    }

    @Test
    @DisplayName("Change name for authorized user")
    public  void changeAuthorizedUserName() {
        user.setName(newName);
        Response response1 = userClient.changeUser(accessToken, user);
        response1.then().assertThat().statusCode(statusCode)
                .and().body("success", equalTo(expected));
        Response response2 = userClient.findUser(accessToken);
        response2.then().assertThat().statusCode(statusCode)
                .and().body("user.name", equalTo(newName));
    }

    @Test
    @DisplayName("Change email for authorized user")
    public void  changeAuthorizedUserEmail(){
        user.setEmail(newEmail);
        Response response1 = userClient.changeUser(accessToken, user);
        response1.then().assertThat().statusCode(statusCode)
                .and().body("success", equalTo(expected));
        Response response2 = userClient.findUser(accessToken);
        response2.then().assertThat().statusCode(statusCode)
                .and().body("user.email", equalTo(newEmail));
    }

    @Test
    @DisplayName("Change password for authorized user")
    public  void changeAuthorizedUserPassword(){
        user.setPassword(newPassword);
        Response response1 = userClient.changeUser(accessToken, user);
        response1.then().assertThat().statusCode(statusCode)
                .and().body("success", equalTo(expected));
        Response response2 = userClient.loginUser(Credentials.from(user));
        response2.then().assertThat().statusCode(statusCode).and().body("success", equalTo(expected));
    }

    @Test
    @DisplayName("Change email for busy one for authorized user")
    public void  changeAuthorizedUserEmailForBusyOne(){
        Response response1 = userClient.findUser(accessToken);
        String emailDefault = response1.then().extract().path("user.email");
        user =  UserGenerator.getAnother();
        Response response2 = userClient.createUser(user);
        String accessToken = response2.then().extract().path("accessToken");
        user.setEmail(emailDefault);
        Response response3 = userClient.changeUser(accessToken, user);
        response3.then().assertThat().statusCode(statusCodeEmailError)
                .and().body("success", equalTo(expectedError))
                .and().body("message", equalTo(emailErrorMessage));
    }

    @Test
    @DisplayName("Change name for not authorized user")
    public  void changeNotAuthorizedUserName(){
        user.setName(newName);
        String accessToken = "";
        Response response1 = userClient.changeUser(accessToken, user);
        response1.then().assertThat().statusCode(statusCodeLoginError)
                .and().body("message", equalTo(loginErrorMessage));
    }

    @Test
    @DisplayName("Change password for not authorized user")
    public  void changeNotAuthorizedUserPassword(){
        user.setPassword(newPassword);
        String accessToken = "";
        Response response1 = userClient.changeUser(accessToken, user);
        response1.then().assertThat().statusCode(statusCodeLoginError)
                .and().body("message", equalTo(loginErrorMessage));
    }

    @Test
    @DisplayName("Change email for not authorized user")
    public void  changeNotAuthorizedUserEmail(){
        user.setEmail(newEmail);
        String accessToken = "";
        Response response1 = userClient.changeUser(accessToken, user);
        response1.then().assertThat().statusCode(statusCodeLoginError)
                .and().body("message", equalTo(loginErrorMessage));
    }


    @After
    public void delete() {
        userClient.deleteUser(accessToken);
    }
}