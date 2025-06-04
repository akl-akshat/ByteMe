import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Admin admin = new Admin("admin", "admin123"); // Default admin credentials
    static ArrayList<Customer> customers = new ArrayList<>();
    public static List<MenuItem> menu = new ArrayList<>();
    private static HashMap<Complaint, Customer> complaints = new HashMap<>();
    public static List<Order> orders = new ArrayList<>(); // Declare orders list
    private static double totalSales = 0.0; // Declare totalSales variable
    public static double getTotalSales() {
        return totalSales;
    }

    public static void setTotalSales(double totalSales) {
        Main.totalSales = totalSales;
    }

    public static Admin getAdmin() {
        return admin;
    }

    public static void setAdmin(Admin admin) {
        Main.admin = admin;
    }
    static {
        // Initialize customers with pre-existing names and passwords
        customers.add(new Customer("akshat", "lakhera"));
        customers.add(new Customer("rachit", "bhandari"));
        customers.add(new Customer("bhavik", "garg"));

        // Initialize menu with some items
        menu.add(new MenuItem("Pasta", 60, "Food", true));
        menu.add(new MenuItem("Samosa", 10, "Food", true));
        menu.add(new MenuItem("Maggi", 20, "Food", true));
        menu.add(new MenuItem("CholeBhature", 40, "Food", true));
        menu.add(new MenuItem("PavBhaji", 50, "Food", true));
        menu.add(new MenuItem("Mirinda", 20, "Drink", true));
        menu.add(new MenuItem("ThumbsUp", 20, "Drink", true));
        menu.add(new MenuItem("Sprite", 40, "Drink", true));
        menu.add(new MenuItem("Water", 20, "Drink", false));
    }


    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome to the Bybyte Portal");
            System.out.println("1. Login as Admin");
            System.out.println("2. Login as Customer");
            System.out.println("3. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    handleAdminLogin();
                    break;
                case 2:
                    handleCustomerLogin();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void handleAdminLogin() {
        try {
            System.out.println("Admin Login");
            System.out.println("Enter username:");
            String username = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();

            if (admin.login(username, password)) {
                adminMenu();
            }
        } catch (InvalidLoginException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void handleCustomerLogin() {
        System.out.println("Customer Login");
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        Customer customer = null;
        for (Customer c : customers) {
            if (c.getUsername().equals(username) && c.getPassword().equals(password)) {
                customer = c;
                break;
            }
        }

        if (customer == null) {
            System.out.println("Invalid customer credentials.");
            System.out.println("Do you want to register? (yes/no)");
            String registerChoice = scanner.nextLine();
            if (registerChoice.equalsIgnoreCase("yes")) {
                customers.add(new Customer(username, password));
                System.out.println("Registration successful. You can now log in with your new credentials.");
            } else {
                return;
            }
        } else {
            while (true) {
                System.out.println("Customer " + customer.getUsername() + " ByByte Menu (VIP: " + customer.isVIP() + ")");
                System.out.println("1) View menu");
                System.out.println("2) Add items to cart");
                System.out.println("3) View your cart");
                System.out.println("4) Remove from cart");
                System.out.println("5) View order status");
                System.out.println("6) Search functionality");
                System.out.println("7) View cart items total");
                System.out.println("8) View menu sorted by price");
                System.out.println("9) Place order");
                System.out.println("10) Order History");
                System.out.println("11) Search Item");
                System.out.println("12) Provide Review");
                System.out.println("13) View Reviews");

                if (customer.isVIP()) {
                    System.out.println("14) Cancel placed order");
                    System.out.println("15) Exit");
                } else {
                    System.out.println("14) Upgrade to VIP");
                    System.out.println("15) Cancel placed order");
                    System.out.println("16) Exit");
                }
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        customer.viewMenu();
                        break;
                    case 2:
                        customer.addItemsToCart();
                        break;
                    case 3:
                        customer.viewCart();
                        break;
                    case 4:
                        customer.removeFromCart();
                        break;
                    case 5:
                        customer.viewOrderStatus();
                        break;
                    case 6:
                        customer.searchMenu();
                        break;
                    case 7:
                        customer.viewCartTotal();
                        break;
                    case 8:
                        Customer.viewMenuSortedByPrice();
                        break;
                    case 9:
                        customer.placeOrder();
                        break;
                    case 10:
                        customer.viewOrderHistory();
                        break;
                    case 11:
                        customer.searchItem();
                        break;
                    case 12:
                        customer.provideReview();
                        break;
                    case 13:
                        customer.viewReviews();
                        break;
                    case 14:
                        if (customer.isVIP()) {
                            System.out.println("Enter the Order ID to cancel:");
                            int orderId = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            customer.cancelOrder(orderId);
                        } else {
                            customer.upgradeToVIP();
                        }
                        break;
                    case 15:
                        if (customer.isVIP()) {
                            System.out.println("Exiting...");
                            return;
                        } else {
                            System.out.println("Enter the Order ID to cancel:");
                            int orderId = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            customer.cancelOrder(orderId);
                        }
                        break;
                    case 16:
                        if (!customer.isVIP()) {
                            System.out.println("Exiting...");
                            return;
                        }
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("Admin Menu");
            System.out.println("1) Add an item to the menu");
            System.out.println("2) Delete an item from the menu");
            System.out.println("3) View pending orders");
            System.out.println("4) Process next order");
            System.out.println("5) View menu");
            System.out.println("6) Update menu item");
            System.out.println("7) View item-wise order history");
            System.out.println("8) View daily sales report");
            System.out.println("9) Cancel Order");
            System.out.println("10) Logout");


            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addItemToMenu();
                    break;
                case 2:
                    removeItemFromMenu();
                    break;
                case 3:
                    admin.viewPendingOrders();
                    break;
                case 4:
                    admin.processNextOrder();
                    break;
                case 5:
                    admin.viewMenu();
                    break;
                case 6:
                    updateMenuItem();
                    break;
                case 7:
                    admin.viewDetailedItemHistory();
                    break;
                case 8:
                    admin.generateDailySalesReport();
                    break;
                case 9:
                    admin.cancelOrder();
                    break;
                case 10:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void updateMenuItem() {
        Admin.updateMenuItem(menu);
    }

    private static void addItemToMenu() {
        System.out.println("Enter item name:");
        String name = scanner.nextLine();
        System.out.println("Enter item price:");
        double price = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter item category:");
        String category = scanner.nextLine();
        System.out.println("Is the item available? (true/false):");
        boolean availability = scanner.nextBoolean();
        scanner.nextLine();

        MenuItem item = new MenuItem(name, price, category, availability);
        menu.add(item);
        System.out.println("Item added to menu.");
    }

    private static void removeItemFromMenu() {
        System.out.println("Current Menu:");
        System.out.printf("%-5s %-20s %-10s %-10s %-10s%n", "S.No", "Name", "Price", "Category", "Availability");
        System.out.println("-------------------------------------------------------------");
        int serialNumber = 1;
        for (MenuItem item : menu) {
            System.out.printf("%-5d %-20s %-10.2f %-10s %-10s%n", serialNumber++, item.getName(), item.getPrice(), item.getCategory(), item.isAvailable() ? "Yes" : "No");
        }

        System.out.println("Enter the serial number of the item to remove:");
        int serialNumberToRemove = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (serialNumberToRemove > 0 && serialNumberToRemove <= menu.size()) {
            MenuItem removedItem = menu.remove(serialNumberToRemove - 1);
            System.out.println("Item removed from menu.");

            // Update the status of all pending orders containing the removed item to 'Cancelled'
            for (Order order : orders) {
                if (order.getStatus().equals("Pending")) {
                    for (MenuItem item : order.getItems()) {
                        if (item.equals(removedItem)) {
                            order.setStatus("Cancelled");
                            System.out.println("Order ID " + order.getId() + " has been cancelled due to item removal.");
                            break;
                        }
                    }
                }
            }
        } else {
            System.out.println("Invalid serial number. Please try again.");
        }
    }
    public static Scanner getScanner() {
        return scanner;
    }
}