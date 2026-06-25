package model;

public class Seminar extends Event
        implements Refundable {

    private String speaker;

    public Seminar(String id, String name, String venueId,
            String organizerId, String date,
            double basePrice, String createdAt, String updatedAt,
            String speaker) {

        super(id, name, venueId, organizerId, date,
                basePrice, createdAt, updatedAt);

        this.speaker = speaker;
    }

    @Override
    public String getEventType() {
        return "seminar";
    }

    @Override
    public double calculateTicketPrice(String category) {

        return getBasePrice();
    }

    @Override
    public double calculateRefund(int daysBeforeEvent) {

        if (daysBeforeEvent > 1) {
            return 1.0;
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

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Speaker: " + speaker;
    }
}