package model;

public class Concert extends Event
        implements Refundable {

    private String artist;

    public Concert(String id, String name, String venueId,
            String organizerId, String date,
            double basePrice, String createdAt, String updatedAt,
            String artist) {

        super(id, name, venueId, organizerId, date,
                basePrice, createdAt, updatedAt);

        this.artist = artist;
    }

    @Override
    public String getEventType() {
        return "concert";
    }

    @Override
    public double calculateTicketPrice(String category) {

        if (category.equalsIgnoreCase("VIP")) {
            return getBasePrice() * 3.0;
        }

        if (category.equalsIgnoreCase("Regular")) {
            return getBasePrice() * 1.0;
        }

        if (category.equalsIgnoreCase("Festival")) {
            return getBasePrice() * 0.7;
        }

        return getBasePrice();
    }

    @Override
    public double calculateRefund(int daysBeforeEvent) {

        if (daysBeforeEvent > 14) {
            return 1.0;
        }

        if (daysBeforeEvent >= 7 && daysBeforeEvent <= 14) {
            return 0.5;
        }

        return 0;
    }

    @Override
    public boolean isRefundable() {
        return true;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Artist: " + artist;
    }
}