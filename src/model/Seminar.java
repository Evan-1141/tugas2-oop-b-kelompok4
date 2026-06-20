package model;

public class Seminar extends Event
        implements Refundable {

    private String speaker;

    public Seminar(String id, String title,
            String location, double price,
            int capacity, String speaker) {

        super(id, title, location, price, capacity);

        this.speaker = speaker;
    }

    @Override
    public String getEventType() {
        return "Seminar";
    }

    @Override
    public double calculateTicketPrice(String category) {

        return getPrice();
    }

    @Override
    public double calculateRefund(int daysBeforeEvent) {

        if (daysBeforeEvent >= 3) {
            return getPrice() * 0.5;
        }

        return 0;
    }

    @Override
    public boolean isRefundable() {
        return true;
    }

    public String getSpeaker() {
        return speaker;
    }
}