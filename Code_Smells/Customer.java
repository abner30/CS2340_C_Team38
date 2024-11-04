public class OrderConfirmationService {
    private final EmailSender emailSender;

    public OrderConfirmationService(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendOrderConfirmation(Order order) {
        String message = createConfirmationMessage(order);
        emailSender.sendEmail(order.getCustomerEmail(), "Order Confirmation", message);
    }

    private String createConfirmationMessage(Order order) {
        StringBuilder message = new StringBuilder();
        message.append("Thank you for your order, ").append(order.getCustomerName()).append("!\n\n");
        message.append("Your order details:\n");

        for (Item item : order.getItems()) {
            message.append(formatItemDetail(item));
        }
        message.append("Total: ").append(order.calculateTotalPrice());

        return message.toString();
    }

    private String formatItemDetail(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        return String.format("%s - %.2f%n", item.getName(), item.getPrice());
    }
}
