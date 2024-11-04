import java.util.List;

public class Order {
    private List<Item> items;
    private Customer customer;

    public Order(List<Item> items, String customerName, String customerEmail) {
        if (customerName == null || customerEmail == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        this.items = items;
        this.customer = new Customer(customerName, customerEmail);

    }

    public double calculateTotalPrice() {
        return new OrderPriceCalculator(this).calculateTotal();
    }

    private String formatItemDetail(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        String toreturn = String.format("%s - %.2f%n", item.getName(), item.getPrice());
        toreturn = toreturn.strip();
        return toreturn;
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
        return customer.getCustomerName();
    }

    public String getCustomerEmail() {
        return customer.getCustomerEmail();
    }

    public void printOrder() {
        System.out.println("Order Details:");
        for (Item item : items) {
            System.out.println(formatItemDetail(item));
        }
    }
}
