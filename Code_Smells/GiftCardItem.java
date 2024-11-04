public class GiftCardItem extends Item {
    public GiftCardItem(String name, double price, int quantity, DiscountType discountType, double discountAmount) {
        super(name, price, quantity, discountType, discountAmount);
    }

    @Override
    public double getPrice() {
        // The price of a gift card represents its discount value
        return super.getPrice();
    }
}
