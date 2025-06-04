import javax.print.MultiDocPrintService;
import java.time.LocalDateTime;
import java.util.List;

public class Order implements Comparable<Order> {
    private static int orderCount = 0;
    private int id;
    private static int idCounter = 1;
    private List<MenuItem> items;
    private boolean isVIP;
    private String status;
    private LocalDateTime orderTime;
    private double totalPrice;
    private String customerName;
    private String specialRequest;


    public Order(List<MenuItem> items, boolean isVIP, String username, String specialRequest) {
        this.id = idCounter++;
        this.items = items;
        this.isVIP = isVIP;
        this.status = "Pending";
        this.orderTime = LocalDateTime.now();
        this.totalPrice = calculateTotalPrice();
        this.customerName = username;
        this.specialRequest = specialRequest;
    }

    private double calculateTotalPrice() {
        //this.viewCart();
        double total = 0;
        for (MenuItem item : items) {
            total += item.getPrice();
        }
        return total;
    }

    public int getId() {
        return id;
    }

    public boolean isVIP() {
        return isVIP;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }
    public List<MenuItem> getItems() {
        return items;
    }

    public String getCustomerName() {
        return customerName;
    }

    @Override
    public int compareTo(Order other) {
        if (this.isVIP && !other.isVIP) {
            return -1;
        } else if (!this.isVIP && other.isVIP) {
            return 1;
        } else {
            return Integer.compare(this.id, other.id);
        }
    }

    public String getOrderDetails() {
        StringBuilder details = new StringBuilder();
        for (MenuItem item : items) {
            details.append(item.getName()).append(" (").append(item.getPrice()).append("), ");
        }
        return details.toString();
    }

    @Override
    public String toString() {
        return "Order ID: " + id + ", VIP: " + isVIP + ", Total Price: Rupees " + totalPrice;
    }

    public Object getCustomer() {
        return customerName;
    }
}