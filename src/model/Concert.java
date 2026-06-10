package model;

public class Concert extends Event
                      implements Refundable {

    private String artist;

    public Concert(int id, String title,
                   String location, double price,
                   int capacity, String artist) {

        super(id, title, location, price, capacity);

        this.artist = artist;
    }

    @Override
    public String getEventType() {
        return "Concert";
    }

    @Override
    public double calculateTicketPrice(String category) {

        if (category.equalsIgnoreCase("VIP")) {
            return getPrice() * 2;
        }

        if (category.equalsIgnoreCase("Festival")) {
            return getPrice() * 0.8;
        }

        return getPrice();
    }

    @Override
    public double calculateRefund(int daysBeforeEvent) {

        if (daysBeforeEvent >= 7) {
            return getPrice() * 0.8;
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

    @Override
    public String toString() {
        return super.toString()
                + " | Artist: " + artist;
    }
}