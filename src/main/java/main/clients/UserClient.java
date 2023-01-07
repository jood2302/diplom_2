package main.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import main.Credentials;
import main.User;

import static io.restassured.RestAssured.given;

public class UserClient extends Client {

    public static final String
            CREATE_USER = "/api/auth/register",
            LOGIN_USER = "/api/auth/login",
            DATA_USER = "/api/auth/user";

    public static final int STATUS_CODE = 202;

    @Step("Create new user")
    public Response createUser(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(CREATE_USER);
    }
    @Step("Login user")
    public Response loginUser(Credentials user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(LOGIN_USER);
    }

    @Step("find user")
    public Response findUser(String accessToken) {
        Response response =
                given()
                        .header("Authorization", accessToken)
                        .spec(getSpec())
                        .when()
                        .get(DATA_USER);
        return response;
    }

    @Step("change user")
    public Response changeUser(String accessToken, User user ) {
        return  given()
                .header("Authorization", accessToken)
                .spec(getSpec())
                .body(user)
                .when()
                .patch(DATA_USER);
    }

    @Step("delete user")
    public void deleteUser(String accessToken) {
        if (accessToken == null) {
            return;
        }
        given()
                .header("Authorization", accessToken)
                .spec(getSpec())
                .when()
                .delete(DATA_USER)
                .then()
                .statusCode(STATUS_CODE);
    }
}