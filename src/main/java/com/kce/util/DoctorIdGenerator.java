package com.kce.util;

import java.util.Random;

public class DoctorIdGenerator {
    public static String generateDoctorId() {
        Random random = new Random();
        int number = 10000 + random.nextInt(90000); // 5-digit
        return "DOC-" + number;
    }
}
