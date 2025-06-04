import java.util.*;

public class Customer extends User {
    private Map<MenuItem, Integer> cart;
    private boolean isVIP;
    private String specialRequest;


    public Customer(String username, String password) {
        super(username, password);
        this.cart = new HashMap<>();
        this.isVIP = false;
    }

    public boolean isVIP() {
        return isVIP;
    }

    public void setVIP(boolean isVIP) {
        this.isVIP = isVIP;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    public void setSpecialRequest(String specialRequest) {
        this.specialRequest = specialRequest;
    }

    public void upgradeToVIP() {
        Scanner scanner = Main.getScanner();
        System.out.println("Are you sure you want to purchase VIP? It will cost you 500 Rs.");
        System.out.println("Wish to proceed? (yes/no)");
        String proceed = scanner.nextLine();

        if (proceed.equalsIgnoreCase("no")) {
            System.out.println("Purchase failed. Returning to customer menu.");
            return;
        } else if (!proceed.equalsIgnoreCase("yes")) {
            System.out.println("Invalid choice. Purchase failed. Returning to customer menu.");
            return;
        }

        System.out.println("Payment Method:");
        System.out.printf("%-5s %-20s%n", "S.No", "Method");
        System.out.println("-------------------------");
        System.out.printf("%-5d %-20s%n", 1, "Credit Card");
        System.out.printf("%-5d %-20s%n", 2, "Debit Card");
        System.out.printf("%-5d %-20s%n", 3, "Net Banking");
        System.out.println("Select a payment method by number:");
        int paymentChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String paymentMethod;
        switch (paymentChoice) {
            case 1:
                paymentMethod = "Credit Card";
                break;
            case 2:
                paymentMethod = "Debit Card";
                break;
            case 3:
                paymentMethod = "Net Banking";
                break;
            default:
                System.out.println("Invalid choice. Purchase failed. Returning to customer menu.");
                return;
        }

        System.out.println("Pay 500 Rs using " + paymentMethod);
        System.out.println("Confirm payment? (yes/no)");
        String confirmPayment = scanner.nextLine();

        if (confirmPayment.equalsIgnoreCase("no")) {
            System.out.println("Payment failed. Returning to customer menu.");
            return;
        } else if (!confirmPayment.equalsIgnoreCase("yes")) {
            System.out.println("Invalid choice. Payment failed. Returning to customer menu.");
            return;
        }

        this.setVIP(true);
        System.out.println("Congratulations! Now you are a Bybyte VIP Customer. Your time is precious to us, your order will be our priority and will be served at the earliest possible.");
    }

    public void viewMenu() {
        System.out.printf("%-20s %-10s %-10s %-10s%n", "Name", "Price", "Category", "Availability");
        System.out.println("-------------------------------------------------------------");
        for (MenuItem item : Main.menu) {
            System.out.printf("%-20s %-10.2f %-10s %-10s%n", item.getName(), item.getPrice(), item.getCategory(), item.isAvailable() ? "Yes" : "No");
        }
    }

    public void searchItem() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the item name to search:");
        String itemName = scanner.nextLine();

        MenuItem foundItem = null;
        for (MenuItem item : Main.menu) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                foundItem = item;
                break;
            }
        }

        if (foundItem != null) {
            System.out.println("Yes,"+foundItem.getName()+" is in the menu");
            System.out.printf("%-20s %-20s%n", "Item Name", foundItem.getName());
            System.out.printf("%-20s %-20s%n", "Price", "INR" + foundItem.getPrice());
            System.out.printf("%-20s %-20s%n", "Category", foundItem.getCategory());
            System.out.printf("%-20s %-20s%n", "Availability", foundItem.isAvailable() ? "Yes" : "No");
        } else {
            System.out.println("Item not found in the menu.");
        }
    }

    public void placeOrder() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. Add items to your cart before placing an order.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        String specialRequest = "";

        System.out.println("Do you have any special requests for your order? (yes/no):");
        String specialRequestChoice = scanner.nextLine();
        if (specialRequestChoice.equalsIgnoreCase("yes")) {
            System.out.println("Enter your special request:");
            specialRequest = scanner.nextLine();
        }
        else if(!specialRequestChoice.equalsIgnoreCase("no")) {
            System.out.println("Invalid choice. Order cancelled.");
            return;
        }
        else{
            specialRequest = "NONE ";
        }

        System.out.println("Akshat Bybyte bill:");
        double totalCartValue = getCartTotal();
        System.out.println("Total Cart Value: Rupees " + totalCartValue);

        System.out.println('\n' + "please pay the bill to place the order");
        System.out.println("Enter 1 to confirm payment and place order, 0 to cancel");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (choice == 0) {
            System.out.println("Order cancelled.");
            return;
        } else if (choice != 1) {
            System.out.println("Invalid choice. Order cancelled.");
            return;
        }

        System.out.println("Payment Method:");
        System.out.printf("%-5s %-20s%n", "S.No", "Method");
        System.out.println("-------------------------");
        System.out.printf("%-5d %-20s%n", 1, "Credit Card");
        System.out.printf("%-5d %-20s%n", 2, "Debit Card");
        System.out.printf("%-5d %-20s%n", 3, "Net Banking");
        System.out.println("Select a payment method by number:");
        int paymentChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        String paymentMethod;
        switch (paymentChoice) {
            case 1:
                paymentMethod = "Credit Card";
                break;
            case 2:
                paymentMethod = "Debit Card";
                break;
            case 3:
                paymentMethod = "Net Banking";
                break;
            default:
                System.out.println("Invalid choice. Order cancelled.");
                return;
        }

        System.out.println("Pay bill of amount " + totalCartValue + "INR, using " + paymentMethod);
        System.out.println("Enter the OTP received on your registered mobile number:");
        int otp = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Payment confirmed. Placing order...");

        Order order = new Order(getCartItems(), isVIP(), getUsername(), specialRequest);
        Main.orders.add(order);
        Main.setTotalSales(Main.getTotalSales() + order.getTotalPrice());
        Main.getAdmin().addOrderToQueue(order);
        System.out.println("Order placed. Order ID: " + order.getId());

        // Clear the customer's cart after placing the order
        clearCart();
    }

    public void provideReview() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Current Menu:");
        System.out.printf("%-5s %-20s %-10s %-10s%n", "S.No", "Name", "Price", "Availability");
        System.out.println("-------------------------------------------------------------");
        int serialNumber = 1;
        for (MenuItem item : Main.menu) {
            System.out.printf("%-5d %-20s %-10.2f %-10s%n", serialNumber++, item.getName(), item.getPrice(), item.isAvailable() ? "Yes" : "No");
        }

        System.out.println("Enter the serial number of the item to review:");
        int serialNumberToReview = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (serialNumberToReview > 0 && serialNumberToReview <= Main.menu.size()) {
            MenuItem selectedItem = Main.menu.get(serialNumberToReview - 1);

            int rating;
            while (true) {
                System.out.println("Rate "+selectedItem.getName()+" on scale 1 to 5:");
                rating = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (rating >= 1 && rating <= 5) {
                    break;
                } else {
                    System.out.println("Invalid rating. Please enter a rating between 1 and 5.");
                }
            }

            System.out.println("Enter your review:");
            String comment = scanner.nextLine();

            Review review = new Review(this.getUsername(), rating, comment);
            selectedItem.addReview(review);
            System.out.println("Review added successfully.");
        } else {
            System.out.println("Invalid serial number. Please try again.");
        }
    }

    public void viewReviews() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Current Menu:");
        System.out.printf("%-5s %-20s %-10s %-10s%n", "S.No", "Name", "Price", "Availability");
        System.out.println("-------------------------------------------------------------------------------------");
        int serialNumber = 1;
        for (MenuItem item : Main.menu) {
            System.out.printf("%-5d %-20s %-10.2f %-10s%n", serialNumber++, item.getName(), item.getPrice(), item.isAvailable() ? "Yes" : "No");
        }

        System.out.println("Enter the serial number of the item to view reviews:");
        int serialNumberToView = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (serialNumberToView > 0 && serialNumberToView <= Main.menu.size()) {
            MenuItem selectedItem = Main.menu.get(serialNumberToView - 1);
            List<Review> reviews = selectedItem.getReviews();

            if (reviews.isEmpty()) {
                System.out.println("No reviews available for this item.");
            } else {
                double averageRating = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
                System.out.println("Average Rating: " + averageRating);
                System.out.println("Reviews:");
                System.out.printf("%-10s %-50s %-20s%n", "Rating", "Comment", "Who Commented");
                System.out.println("-------------------------------------------------------------");
                for (Review review : reviews) {
                    System.out.printf("%-10d %-50s %-20s%n", review.getRating(), review.getComment(), review.getCustomerName());
                }
            }
        } else {
            System.out.println("Invalid serial number. Please try again.");
        }
    }

    public void addItemsToCart() {
        viewMenu();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the item names to add to cart (space-separated):");
        String[] itemNames = scanner.nextLine().split(" ");

        for (String itemName : itemNames) {
            MenuItem foundItem = null;
            for (MenuItem item : Main.menu) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    foundItem = item;
                    break;
                }
            }

            if (foundItem != null) {
                if (foundItem.isAvailable()) {
                    cart.put(foundItem, cart.getOrDefault(foundItem, 0) + 1);
                    System.out.println(foundItem.getName() + " added to cart.");
                } else {
                    System.out.println(foundItem.getName() + " is not available currently, so it cannot be added to the cart.");
                }
            } else {
                System.out.println(itemName + " not found in the menu.");
            }
        }
    }

    private MenuItem findItemByName(String itemName) {
        for (MenuItem item : Main.menu) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    public void viewCart() {
        System.out.println("Customer " + getUsername() + " Cart:");
        System.out.printf("%-5s %-20s %-10s %-10s %-10s%n", "S.No", "Item Name", "Price", "Category", "Quantity");
        System.out.println("-------------------------------------------------------------");
        int serialNumber = 1;
        for (Map.Entry<MenuItem, Integer> entry : cart.entrySet()) {
            MenuItem item = entry.getKey();
            int quantity = entry.getValue();
            System.out.printf("%-5d %-20s %-10.2f %-10s %-10d%n", serialNumber++, item.getName(), item.getPrice(), item.getCategory(), quantity);
        }
    }

    public void removeFromCart() {
        viewCart();
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the serial number of the item to remove:");
        int serialNumber = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (serialNumber > 0 && serialNumber <= cart.size()) {
            int index = 1;
            MenuItem itemToRemove = null;
            for (Map.Entry<MenuItem, Integer> entry : cart.entrySet()) {
                if (index == serialNumber) {
                    itemToRemove = entry.getKey();
                    break;
                }
                index++;
            }
            if (itemToRemove != null) {
                if (cart.get(itemToRemove) > 1) {
                    cart.put(itemToRemove, cart.get(itemToRemove) - 1);
                } else {
                    cart.remove(itemToRemove);
                }
                System.out.println(itemToRemove.getName() + " removed from cart.");
            }
        } else {
            System.out.println("Invalid serial number. Please try again.");
        }
    }

    public void viewOrderStatus() {
        boolean orderFound = false;
        for (Order order : Main.orders) {
            if (order.getCustomerName().equals(getUsername())) {
                System.out.println("Order ID: " + order.getId() + ", Order Status: " + order.getStatus());
                orderFound = true;
            }
        }
        if (!orderFound) {
            System.out.println("You haven't ordered anything yet.");
            System.out.println("Enter 1 to browse menu, 0 to exit:");
            int choice = Main.getScanner().nextInt();
            Main.getScanner().nextLine(); // Consume newline
            if (choice == 1) {
                viewMenu();
            }
        }
    }

    public void viewOrderHistory() {
        System.out.println("Customer " + this.getUsername() + " Order History:");
        System.out.printf("%-10s %-20s %-10s%n", "Order ID", "Customer Name", "Status");
        System.out.println("-------------------------------------------------------------");
        for (Order order : Main.orders) {
            if (order.getCustomerName().equals(this.getUsername()) && (order.getStatus().equals("Cancelled") || order.getStatus().equals("Completed"))) {
                System.out.printf("%-10d %-20s %-10s%n", order.getId(), order.getCustomerName(), order.getStatus());
            }
        }
    }

    public void searchMenu() {
        Set<String> categories = new HashSet<>();
        for (MenuItem item : Main.menu) {
            categories.add(item.getCategory());
        }

        System.out.println("Available categories:");
        int index = 1;
        for (String category : categories) {
            System.out.println(index++ + ") " + category);
        }

        System.out.println("Choose a category by number:");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (choice > 0 && choice <= categories.size()) {
            List<MenuItem> itemsInCategory = new ArrayList<>();
            for (MenuItem item : Main.menu) {
                if (item.getCategory().equalsIgnoreCase(new ArrayList<>(categories).get(choice - 1))) {
                    itemsInCategory.add(item);
                }
            }
            System.out.printf("%-20s %-10s %-10s %-10s%n", "Name", "Price", "Category", "Availability");
            System.out.println("-------------------------------------------------------------");
            for (MenuItem item : itemsInCategory) {
                System.out.printf("%-20s %-10.2f %-10s %-10s%n", item.getName(), item.getPrice(), item.getCategory(), item.isAvailable() ? "Yes" : "No");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }


    public void clearCart() {
        cart.clear();
    }

    public void viewCartTotal() {
        System.out.println("Customer " + getUsername() + " Cart:");
        System.out.printf("%-5s %-20s %-10s %-10s%n", "S.No", "Item Name", "Price", "Quantity");
        System.out.println("-------------------------------------------------------------");
        int serialNumber = 1;
        for (Map.Entry<MenuItem, Integer> entry : cart.entrySet()) {
            MenuItem item = entry.getKey();
            int quantity = entry.getValue();
            System.out.printf("%-5d %-20s %-10.2f %-10d%n", serialNumber++, item.getName(), item.getPrice(), quantity);
        }

        double total = 0;
        for (Map.Entry<MenuItem, Integer> entry : cart.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        System.out.println("Total Cart Value: Rupees " + total);
    }

    public static void viewMenuSortedByPrice() {
        List<MenuItem> sortedMenu = new ArrayList<>(Main.menu);
        sortedMenu.sort(Comparator.comparingDouble(MenuItem::getPrice));
        System.out.printf("%-20s %-10s %-10s %-10s%n", "Name", "Price", "Category", "Availability");
        System.out.println("-------------------------------------------------------------");
        for (MenuItem item : sortedMenu) {
            System.out.printf("%-20s %-10.2f %-10s %-10s%n", item.getName(), item.getPrice(), item.getCategory(), item.isAvailable() ? "Yes" : "No");
        }
    }

    public void orderFood() {
        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. Add items to your cart before placing an order.");
            return;
        }
        System.out.println("Order placed successfully! Your order details:");
        viewCart();
        cart.clear(); // Clear cart after ordering
    }

    public void cancelOrder(int orderId) {
        for (Order order : Main.orders) {
            if (order.getId() == orderId && order.getStatus().equals("Pending") && order.getCustomerName().equals(this.getUsername())) {
                order.setStatus("Cancelled");
                double refundAmount = order.getTotalPrice();
                System.out.println("Order ID " + orderId + " has been cancelled.");
                System.out.println("Refunded total amount of Rupees " + refundAmount + " to " + this.getUsername());
                return;
            } else if (order.getStatus().equals("Completed")) {
                System.out.println("Order ID " + orderId + " has already been processed and cannot be cancelled.");
                return;
            } else if (order.getStatus().equals("Cancelled")) {
                System.out.println("Order ID " + orderId + " has already been cancelled.");
                return;
            } else if (order.getStatus().equals("Pending") && !order.getCustomerName().equals(this.getUsername())) {
                System.out.println("Order ID " + orderId + " cannot be cancelled. It was placed by another customer.");
                return;
            }
        }

        System.out.println("Order ID " + orderId + " cannot be cancelled. It may not exist or has already been processed.");
    }
    public double getCartTotal() {
        this.viewCart();
        double total = 0;
        for (Map.Entry<MenuItem, Integer> entry : cart.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return  total;
    }

    public List<MenuItem> getCartItems() {
        List<MenuItem> items = new ArrayList<>();
        for (Map.Entry<MenuItem, Integer> entry : cart.entrySet()) {
            for (int i = 0; i < entry.getValue(); i++) {
                items.add(entry.getKey());
            }
        }
        return items;
    }
}