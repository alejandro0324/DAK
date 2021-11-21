package application.dak.DAK.backend.common.models;

import application.dak.DAK.backend.packages.services.PackagePayment;

import java.util.Random;

public class StrategyPaymentCredit implements StrategyPayment {

    public String execute() {
        Random rand = new Random();
        int upperbound = 25;
        int int_random = rand.nextInt(upperbound);
        if (int_random <= 18) {
            return "Post: OK";
        }
        if (int_random < 22) {
            return "Post: ERROR";
        }
        if (int_random > 22) {
            return "Post: Try Again";
        }
        return "0";
    }
}
