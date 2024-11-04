public class OrderPriceCalculator {
    private final Order order;
    private static final double BULK_ORDER_THRESHOLD = 100.0;
    private static final double BULK_ORDER_DISCOUNT = 0.9; // 10% discount

    /**
     * Constructs an OrderPriceCalculator for a given order.
     *
     * @param order The order for which the total price is calculated.
     */
    public OrderPriceCalculator(Order order) {
        this.order = order;
    }

    /**
     * Calculates the total price of the order, applying any item-level discounts,
     * tax, gift card discounts, and bulk order discounts as applicable.
     *
     * @return The total calculated price for the order.
     */
    public double calculateTotal() {
        double total = 0.0;

        // Calculate base total with discounts and tax
        for (Item item : order.getItems()) {
            double price = item.getPrice();

            // Apply item-level discount first
            switch (item.getDiscountType()) {
                case PERCENTAGE:
                    price -= item.getDiscountAmount() * price;
                    break;
                case AMOUNT:
                    price -= item.getDiscountAmount();
                    break;
            }

            // Multiply by quantity
            price *= item.getQuantity();

            // Add tax if applicable
            if (item instanceof TaxableItem) {
                TaxableItem taxableItem = (TaxableItem) item;
                double tax = taxableItem.getTaxRate() / 100.0 * item.getPrice();
                price += tax;
            }

            total += price;
        }

        // Apply gift card discount if present
        if (hasGiftCard()) {
            total -= 10.0;
        }

        // Apply bulk discount if total is over threshold
        if (total > BULK_ORDER_THRESHOLD) {
            total *= BULK_ORDER_DISCOUNT;
        }

        return total;
    }

    /**
     * Checks if there is a gift card in the order.
     *
     * @return True if the order contains a gift card item, false otherwise.
     */
    private boolean hasGiftCard() {
        return order.getItems().stream().anyMatch(item -> item instanceof GiftCardItem);
    }
}
