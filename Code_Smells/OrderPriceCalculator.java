public class OrderPriceCalculator {
    private final Order order;
    private static final double BULK_ORDER_THRESHOLD = 100.0;
    private static final double BULK_ORDER_DISCOUNT = 0.9; // 10% discount

    public OrderPriceCalculator(Order order) {
        this.order = order;
    }

    public double calculateTotal() {
        double total = calculateItemsTotal();
        total = applyGiftCardDiscount(total);
        total = applyBulkDiscount(total);
        return total;
    }

    private double calculateItemsTotal() {
        double total = 0.0;
        for (Item item : order.getItems()) {
            total += calculateItemPrice(item);
        }
        return total;
    }

    private double calculateItemPrice(Item item) {
        double price = item.getPrice();
        price = applyItemDiscount(item, price);
        price *= item.getQuantity();

        if (item instanceof TaxableItem) {
            price = applyTax(price, (TaxableItem) item);
        }

        return price;
    }

    private double applyItemDiscount(Item item, double price) {
        switch (item.getDiscountType()) {
            case PERCENTAGE:
                return price * (1 - item.getDiscountAmount());
            case AMOUNT:
                return price - item.getDiscountAmount();
            default:
                return price;
        }
    }

    private double applyTax(double price, TaxableItem item) {
        return price * (1 + item.getTaxRate() / 100.0);
    }

    private double applyGiftCardDiscount(double total) {
        double giftCardValue = calculateGiftCardValue();
        return Math.max(0, total - giftCardValue);
    }

    private double calculateGiftCardValue() {
        double totalGiftCardValue = 0.0;
        for (Item item : order.getItems()) {
            if (item instanceof GiftCardItem) {
                totalGiftCardValue += item.getPrice();
            }
        }
        return totalGiftCardValue;
    }

    private double applyBulkDiscount(double total) {
        return total > BULK_ORDER_THRESHOLD ? total * BULK_ORDER_DISCOUNT : total;
    }
}
