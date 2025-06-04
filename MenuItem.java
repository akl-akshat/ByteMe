import java.util.ArrayList;
import java.util.List;
public class MenuItem {
    private String name;
    private double price;
    private String category;
    private boolean availability;
    private int quantity;
    private List<Review> reviews = new ArrayList<>();

    public MenuItem(String name, double price, String category, boolean availability) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.availability = availability;
        this.quantity = 1;
    }


    public List<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return availability;
    }

    public void setAvailable(boolean available) {
        this.availability = available;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", availability=" + availability +
                '}';
    }
}