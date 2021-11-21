package application.dak.DAK.backend.packages.services;

import application.dak.DAK.backend.common.models.*;

public class PackagePayment {
    private StrategyPayment strategyPayment;

    public String getResult() {
        return strategyPayment.execute();
    }

    public void setStrategy(String type) {
        switch (type) {
            case "Credit":
                this.strategyPayment = new StrategyPaymentCredit();
                break;
            case "Debit":
                this.strategyPayment = new StrategyPaymentDebit();
                break;
            case "MercadoPago":
                this.strategyPayment = new StrategyPaymentMercadoPago();
                break;
        }
    }
}
