import java.util.List;

public class Order {
    private List<Item> items;
    private String customerName;
    private String customerEmail;

    public Order(List<Item> items, String customerName, String customerEmail) {
        this.items = items;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }

    public double calculateTotalPrice() {
        return new OrderPriceCalculator(this).calculateTotal();
    }

    public void sendConfirmationEmail() {
        String message = createConfirmationMessage();
        EmailSender.sendEmail(customerEmail, "Order Confirmation", message);
    }

    private String createConfirmationMessage() {
        StringBuilder message = new StringBuilder();
        message.append("Thank you for your order, ").append(customerName).append("!\n\n");
        message.append("Your order details:\n");

        for (Item item : items) {
            message.append(formatItemDetail(item));
        }
        message.append("Total: ").append(calculateTotalPrice());

        return message.toString();
    }

    private String formatItemDetail(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        return String.format("%s - %.2f%n", item.getName(), item.getPrice());
    }

    public void addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        items.add(item);
    }

    public void removeItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        items.remove(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void printOrder() {
        System.out.println("Order Details:");
        for (Item item : items) {
            System.out.println(formatItemDetail(item));
        }
    }
}
