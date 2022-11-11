import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        File basketFile = new File("basket.txt");

        Product[] products = {new Product(1, "Хлеб", 50),
                new Product(2, "Молоко", 70),
                new Product(3, "Сок", 100)};

        System.out.println("Продукты которые вы можете добавить в корзину:");
        for (Product product : products) {
            System.out.println(product);
        }
        System.out.println();

        String[] productNames = new String[products.length];
        long[] prices = new long[products.length];
        long[] amounts = new long[products.length];
        Basket basket = new Basket(productNames, prices);
        basket.setAmounts(amounts);

        if (basketFile.exists()) {
            basket = Basket.loadFromTxtFile(basketFile);
            basket.printCart();
            productNames = basket.getProductNames();
            prices = basket.getPrices();
        }

        while (true) {
            System.out.println("Введите номер товара и его количество через пробел \n" +
                    "или введите end для окончания работы программы");
            String input = scanner.nextLine();

            if ("end".equalsIgnoreCase(input)) {
                basket.printCart();
                break;
            } else {
                String[] parts = input.split(" ");
                int productNumber = Integer.parseInt(parts[0]) - 1;
                int productCount = Integer.parseInt(parts[1]);
                if (basket.getPrices()[productNumber] == 0) {
                    prices[productNumber] = products[productNumber].getPrice();
                    productNames[productNumber] = products[productNumber].getName();
                    basket.setPrices(prices);
                    basket.setProductNames(productNames);
                }
                basket.addToCart(productNumber, productCount);
                basket.saveTxt(basketFile);
            }
        }
    }
}
