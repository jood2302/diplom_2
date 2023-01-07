package main.clients;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class Client {
    private  static final String  BASE_URL = "https://stellarburgers.nomoreparties.site";
    protected RequestSpecification getSpec(){
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("Status code <403> but was <429>, due to the high number of requests");
        }
        return new RequestSpecBuilder()
                .setContentType("application/json")
                .setBaseUri(BASE_URL)
                .build();
    }
}