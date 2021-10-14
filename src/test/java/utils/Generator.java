package utils;

import restTests.pojos.doRegister.UserRequest;

import java.util.Random;

public class Generator {

    public static UserRequest generateUser() {
        String randomNumber = getRandomNumber();
        return UserRequest.builder()
                .name(randomNumber)
                .email(randomNumber + "@mail.ru")
                .password(randomNumber)
                .build();
    }

    public static String getRandomNumber() {
        return String.valueOf(System.currentTimeMillis());
    }

    public static String getRandomString(int length) {
        String pool = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(62);
            stringBuilder.append(pool.charAt(index));
        }
        return stringBuilder.toString().toLowerCase();
    }

}
