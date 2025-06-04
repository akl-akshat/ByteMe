import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;

public class Admin extends User {
    private PriorityQueue<Order> orderQueue;
    private static Scanner scanner = new Scanner(System.in);

    public Admin(String username, String password) {
        super(username, password);
        this.orderQueue = new PriorityQueue<>();  // PriorityQueue to manage orders based on priority
    }

    // Adds a new order to the queue
    public void addOrderToQueue(Order order) {
        orderQueue.add(order);
        System.out.println("Order added to queue: " + order);
    }

    // Processes the next order in the queue
    public void processNextOrder() {
        if (orderQueue.isEmpty()) {
            System.out.println("No pending orders to process.");
            return;
        }

        Order order = orderQueue.poll();
        //if order status is cancelled, skip the order
        if(order.getStatus().equals("Cancelled")){
            System.out.println("Order ID " + order.getId() +"of amount "+ order.getTotalPrice() + " has been cancelled.");
            return;
        }
        System.out.println("Processing Order ID: " + order.getId() + " (VIP: " + (order.isVIP() ? "yes" : "no") + ")");
        System.out.println("Order " + order.getId() + " cart:");
        System.out.println("Customer: " + order.getCustomerName());
        System.out.println("Special Request: " + order.getSpecialRequest());
        Map<String, Integer> itemCount = new HashMap<>();
        for (MenuItem item : order.getItems()) {
            itemCount.put(item.getName(), itemCount.getOrDefault(item.getName(), 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : itemCount.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double itemPrice = Main.menu.stream().filter(i -> i.getName().equals(itemName)).findFirst().get().getPrice();
            System.out.printf("%-30s | %-10.2f%n", itemName + " X " + quantity, itemPrice * quantity);
        }
        order.setStatus("Completed");
        System.out.println("Order ID " + order.getId() + " has been completed.");
    }

    public boolean login(String username, String password) throws InvalidLoginException {
        if (this.getUsername().equals(username) && this.getPassword().equals(password)) {
            return true;
        } else {
            throw new InvalidLoginException("Invalid admin credentials.");
        }
    }

    // Views all pending orders in the queue
    public void viewPendingOrders() {
        System.out.println("Pending Orders:");
        System.out.printf("%-10s | %-5s | %-30s | %-10s | %-10s%n", "Order ID", "VIP", "Order", "Price", "Total Price");
        System.out.println("--------------------------------------------------------------------------------------");
        for (Order order : Main.orders) {
            if (order.getStatus().equals("Pending")) {
                Map<String, Integer> itemCount = new HashMap<>();
                for (MenuItem item : order.getItems()) {
                    itemCount.put(item.getName(), itemCount.getOrDefault(item.getName(), 0) + 1);
                }
                boolean firstItem = true;
                for (Map.Entry<String, Integer> entry : itemCount.entrySet()) {
                    String itemName = entry.getKey();
                    int quantity = entry.getValue();
                    double itemPrice = Main.menu.stream().filter(i -> i.getName().equals(itemName)).findFirst().get().getPrice();
                    if (firstItem) {
                        System.out.printf("%-10d | %-5s | %-30s | %-10.2f | %-10.2f%n", order.getId(), order.isVIP(), itemName + " X " + quantity, itemPrice * quantity, order.getTotalPrice());
                        firstItem = false;
                    } else {
                        System.out.printf("%-10s | %-5s | %-30s | %-10.2f | %-10s%n", "", "", itemName + " X " + quantity, itemPrice * quantity, "");
                    }
                }
                System.out.println("--------------------------------------------------------------------------------------");
            }
        }
    }

    public void generateDailySalesReport() {
        Map<String, Integer> itemOrderCount = new HashMap<>();
        Map<String, Double> itemSales = new HashMap<>();
        double totalSales = 0;
        int totalOrders = 0;

        for (Order order : Main.orders) {
            if (order.getStatus().equals("Completed")) {
                totalOrders++;
                for (MenuItem item : order.getItems()) {
                    itemOrderCount.put(item.getName(), itemOrderCount.getOrDefault(item.getName(), 0) + 1);
                    itemSales.put(item.getName(), itemSales.getOrDefault(item.getName(), 0.0) + item.getPrice());
                    totalSales += item.getPrice();
                }
            }
        }

        System.out.println("Daily Sales Report:");
        System.out.println("Total Sales: Rupees " + totalSales);
        System.out.println("Total Orders: " + totalOrders);
        System.out.println("Item-wise Sales:");
        System.out.printf("%-20s %-10s %-10s%n", "Item Name", "Order Count", "Sales");
        System.out.println("----------------------------------------");

        String mostPopularItem = null;
        int maxOrderCount = 0;
        String highestSalesItem = null;
        double maxSales = 0;

        for (Map.Entry<String, Integer> entry : itemOrderCount.entrySet()) {
            String itemName = entry.getKey();
            int orderCount = entry.getValue();
            double sales = itemSales.get(itemName);

            System.out.printf("%-20s %-10d %-10.2f%n", itemName, orderCount, sales);

            if (orderCount > maxOrderCount) {
                maxOrderCount = orderCount;
                mostPopularItem = itemName;
            }

            if (sales > maxSales) {
                maxSales = sales;
                highestSalesItem = itemName;
            }
        }

        System.out.println("----------------------------------------");
        System.out.println("Most Popular Item: " + mostPopularItem + " (Ordered " + maxOrderCount + " times)");
        System.out.println("Item with Highest Sales: " + highestSalesItem + " (Sales: Rupees " + maxSales + ")");
    }

    public void viewDetailedItemHistory() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Current Menu:");
        System.out.printf("%-5s %-20s %-10s %-10s%n", "S.No", "Name", "Price", "Availability");
        System.out.println("-------------------------------------------------------------");
        int serialNumber = 1;
        for (MenuItem item : Main.menu) {
            System.out.printf("%-5d %-20s %-10.2f %-10s%n", serialNumber++, item.getName(), item.getPrice(), item.isAvailable() ? "Yes" : "No");
        }

        System.out.println("Enter the serial number of the item to view its detailed history:");
        int serialNumberToView = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (serialNumberToView > 0 && serialNumberToView <= Main.menu.size()) {
            MenuItem selectedItem = Main.menu.get(serialNumberToView - 1);
            int totalSold = 0;
            double totalSales = 0.0;

            for (Order order : Main.orders) {
                if (order.getStatus().equals("Completed")) {
                    for (MenuItem item : order.getItems()) {
                        if (item.equals(selectedItem)) {
                            totalSold++;
                            totalSales += item.getPrice();
                        }
                    }
                }
            }

            System.out.println("Detailed Item History:");
            System.out.println("Item Name: " + selectedItem.getName());
            System.out.println("Total Sold: " + totalSold);
            System.out.println("Total Sales: Rupees " + totalSales);
            System.out.println("Currently Available: " + (selectedItem.isAvailable() ? "Yes" : "No"));
        } else {
            System.out.println("Invalid serial number. Please try again.");
        }
    }

    public void viewItemWiseOrderHistory() {
        Map<String, Integer> itemOrderCount = new HashMap<>();
        for (Order order : Main.orders) {
            for (MenuItem item : order.getItems()) {
                itemOrderCount.put(item.getName(), itemOrderCount.getOrDefault(item.getName(), 0) + 1);
            }
        }
        System.out.println("Item-wise Order History:");
        System.out.printf("%-20s %-10s%n", "Item Name", "Order Count");
        System.out.println("-------------------------");
        for (Map.Entry<String, Integer> entry : itemOrderCount.entrySet()) {
            System.out.printf("%-20s %-10d%n", entry.getKey(), entry.getValue());
        }
    }

    // Sample daily sales report generation

    public void cancelOrder() {
        //ask if want to cancel all the existing orders or want a specific order to be cancelled
        System.out.println("Do you want to cancel all the existing orders or want a specific order to be cancelled?");
        System.out.println("1. Cancel all existing orders");
        System.out.println("2. Cancel a specific order");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        if (choice == 1) {
            for (Order order : Main.orders) {
                if (order.getStatus().equals("Pending")) {
                    order.setStatus("Cancelled");
                }
            }
            System.out.println("All pending orders have been cancelled.");
            return;
        }
        else if(choice == 2){
            System.out.println("Enter the order ID to cancel:");
            int orderId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            for (Order order : Main.orders) {
                if (order.getId() == orderId && order.getStatus().equals("Pending")) {
                    order.setStatus("Cancelled");
                    System.out.println("Order ID " + orderId + " has been cancelled.");
                    return;
                }
                else if(order.getId() == orderId && order.getStatus().equals("Cancelled")){
                    System.out.println("Order ID " + orderId + " has already been cancelled.");
                    return;
                }
                else if(order.getId() == orderId && order.getStatus().equals("Completed")){
                    System.out.println("Order ID " + orderId + " has already been processed.");
                    return;
                }
                else{
                    System.out.println("Order ID " + orderId + " not found in the queue.");
                    return;
                }
            }
        }
        else{
            System.out.println("Invalid choice. Please select a valid option.");
        }


        System.out.println("Order ID " + choice + " cannot be cancelled. It may not exist or has already been processed.");
    }

    // Update the order status (if needed)
    public void updateOrderStatus(int orderId, String newStatus) {
        for (Order order : orderQueue) {
            if (order.getId() == orderId) {
                order.setStatus(newStatus);
                System.out.println("Order ID " + orderId + " status updated to: " + newStatus);
                return;
            }
        }
        System.out.println("Order ID " + orderId + " not found in the queue.");
    }

    public void viewMenu() {
        System.out.println("Current Menu:");
        System.out.printf("%-5s %-20s %-10s %-10s %-10s%n", "S.No", "Name", "Price", "Category", "Availability");
        System.out.println("-------------------------------------------------------------");
        int serialNumber = 1;
        for (MenuItem item : Main.menu) {
            System.out.printf("%-5d %-20s %-10.2f %-10s %-10s%n", serialNumber++, item.getName(), item.getPrice(), item.getCategory(), item.isAvailable() ? "Yes" : "No");
        }
    }

    public void addItemToMenu(MenuItem item) {
        Main.menu.add(item);
        System.out.println("Item added to menu: " + item);
    }

    public void removeItemFromMenu(MenuItem item) {
        if (Main.menu.remove(item)) {
            System.out.println("Item removed from menu: " + item);
        } else {
            System.out.println("Item not found in the menu.");
        }
    }

    public static void updateMenuItem(List<MenuItem> menu) {
        Scanner scanner = new Scanner(System.in);

        // Display the current menu items
        System.out.println("Current Menu:");
        System.out.printf("%-5s %-20s %-10s %-10s %-10s%n", "S.No", "Name", "Price", "Category", "Availability");
        System.out.println("-------------------------------------------------------------");
        int serialNumber = 1;
        for (MenuItem item : menu) {
            System.out.printf("%-5d %-20s %-10.2f %-10s %-10s%n", serialNumber++, item.getName(), item.getPrice(), item.getCategory(), item.isAvailable() ? "Yes" : "No");
        }

        // Prompt admin to select an item to update
        System.out.println("Enter the serial number of the item to update:");
        int itemIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume newline

        // Validate the selected item index
        if (itemIndex < 0 || itemIndex >= menu.size()) {
            System.out.println("Error: Invalid item selection. Please select a valid item.");
            return;
        }

        // Get the selected menu item
        MenuItem selectedItem = menu.get(itemIndex);

        // Display update options
        while (true) {
            System.out.println("Select attribute to update:");
            System.out.println("1. Update Name");
            System.out.println("2. Update Price");
            System.out.println("3. Update Category");
            System.out.println("4. Update Availability");
            System.out.println("5. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println("Enter new name:");
                    String newName = scanner.nextLine();
                    selectedItem.setName(newName);
                    System.out.println("Name updated successfully.");
                    break;

                case 2:
                    System.out.println("Enter new price:");
                    double newPrice = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    selectedItem.setPrice(newPrice);
                    System.out.println("Price updated successfully.");
                    break;

                case 3:
                    System.out.println("Enter new category:");
                    String newCategory = scanner.nextLine();
                    selectedItem.setCategory(newCategory);
                    System.out.println("Category updated successfully.");
                    break;

                case 4:
                    System.out.println("Is the item available? (true/false):");
                    boolean newAvailability = scanner.nextBoolean();
                    scanner.nextLine(); // Consume newline
                    selectedItem.setAvailable(newAvailability);
                    System.out.println("Availability updated successfully.");
                    break;

                case 5:
                    System.out.println("Exiting update menu.");
                    return;

                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }
}