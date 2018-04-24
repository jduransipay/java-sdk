package examples;

import org.json.JSONObject;
import sipay.Altp;
import sipay.Amount;
import sipay.paymethod.paypal.Methods;
import sipay.paymethod.paypal.Payment;
import sipay.responses.PaypalMethods;
import sipay.responses.PaypalPayment;

public class Paypal {

    public static void main(String[] args) {
        String path = "config.properties";
        Altp altp = new Altp(path);

        JSONObject payload = new JSONObject();
        JSONObject notify = new JSONObject();
        JSONObject billing = new JSONObject();

        payload.put("order", "aaa-order-123457009877483");
        payload.put("reconciliation", "reconciliation");
        payload.put("title", "Sipay Pruebas");
        payload.put("logo", "url");
        payload.put("customId", "90");

        notify.put("result", "https://requestb.in/uhdq21uh");
        payload.put("notify", notify);

        billing.put("description", "prueba subscription");
        payload.put("billing", billing);
        payload.put("policyData", new JSONObject());

        // region * Petición donde se solicite un token para una suscripción de Pay Pal (sin pago en ese momento) (Billing Agreement sin pago).
        Amount amount = new Amount("0", "EUR");

        Methods methods = new Methods(payload);
        PaypalMethods paypalMethods = altp.paypalMethods(methods, amount);
        if (paypalMethods == null) {
            System.out.println("Failure in operation. Error connecting to the service");
        } else if (paypalMethods.getCode() != 0) {
            System.out.println("Failure in operation. Error:" + paypalMethods.getDescription());
        } else {
            System.out.println("Operation processed successfully");
        }
        // endregion

        // region * Petición de un pago con Pay Pal en el que se solicite un token para una suscripción de Pay Pal (1€)
        Amount amount2 = new Amount("100", "EUR");
        Methods methods2 = new Methods(payload);
        PaypalMethods paypalMethods2 = altp.paypalMethods(methods2, amount2);
        if (paypalMethods2 == null) {
            System.out.println("Failure in operation. Error connecting to the service");
        } else if (paypalMethods2.getCode() != 0) {
            System.out.println("Failure in operation. Error:" + paypalMethods2.getDescription());
        } else {
            System.out.println("Operation processed successfully");
        }
        // endregion

        // region * Cobro recurrente de una suscripción de Pay Pal usando el token.
        JSONObject payload2 = new JSONObject();
        JSONObject notify2 = new JSONObject();

        payload2.put("order", "aaa-order-623457009877483");
        notify2.put("result", "https://requestb.in/uhdq21uh");
        payload2.put("notify", notify2);
        payload2.put("billingId", "B-5YT73212N0953524X");
        payload2.put("reconciliation", "reconciliation");

        Payment payment = new Payment(payload2);
        PaypalPayment paypalPayment = altp.paypalPayment(payment, amount2);
        if (paypalPayment == null) {
            System.out.println("Failure in operation. Error connecting to the service");
        } else if (paypalPayment.getCode() != 0) {
            System.out.println("Failure in operation. Error:" + paypalPayment.getDescription());
        } else {
            System.out.println("Operation processed successfully");
        }
        // endregion
    }
}
