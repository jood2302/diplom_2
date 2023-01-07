package main.generators;

import main.User;

import java.util.Random;

public class UserGenerator {


    static String name = "something" + new Random().nextInt(10000);
    static String email = "something" + new Random().nextInt(10000) + "@bk.ru";
    static String password = "something" + new Random().nextInt(10000);

    public static User getDefault() {
        return new User(name, email, password);
    }
    public static User getAnother() {
        return new User("1"+name, "1"+email, "1"+password);
    }
    public static User getWithoutName() {
        return new User(null, email, password);
    }

    public static User getWithoutEmail() {
        return new User(name, null, password);
    }

    public static User getWithoutPassword() {
        return new User(name, email, null);
    }

    public static User authorization() {
        return new User(null, email, password);
    }
}