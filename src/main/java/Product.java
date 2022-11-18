public class Product {
    private final int number;
    private final String name;
    private final int price;

    public Product(int number, String name, int price) {
        this.number = number;
        this.price = price;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String toString() {
        return number + ". " + name + " - " + price + " руб/шт";
    }
}
