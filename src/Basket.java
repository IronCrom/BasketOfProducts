import java.io.*;
import java.util.Arrays;

public class Basket implements Serializable {

    public long[] getPrices() {
        return prices;
    }

    public void setPrices(long[] prices) {
        this.prices = prices;
    }

    public void setProductNames(String[] productNames) {
        this.productNames = productNames;
    }

    private long[] prices;

    public String[] getProductNames() {
        return productNames;
    }

    private String[] productNames;
    private long[] amounts;

    public Basket(String[] productNames, long[] prices) {
        this.productNames = productNames;
        this.prices = prices;
    }

    public void addToCart(int productNum, int amount) {
        amounts[productNum] += amount;
    }

    public void printCart() {
        if (Arrays.stream(amounts).sum() == 0) {
            System.out.println("Ваша корзина пуста!");
        } else {
            System.out.println("Ваша корзина:");
            long sumBasket = 0;
            int count = 1;
            for (int i = 0; i < amounts.length; i++) {
                if (amounts[i] != 0) {
                    long sumProduct = amounts[i] * prices[i];
                    System.out.println(count + ". " + productNames[i] + " " + amounts[i] + "шт " + sumProduct + " руб в сумме");
                    count++;
                    sumBasket += sumProduct;
                }
            }
            System.out.println("Итого: " + sumBasket + " руб \n");
        }
    }

    public void saveTxt(File textFile) throws IOException {
        try (PrintWriter out = new PrintWriter(textFile)) {
            for (String productName : productNames)
                out.print(productName + " ");
            out.println();
            for (long price : prices)
                out.print(price + " ");
            out.println();
            for (long amount : amounts)
                out.print(amount + " ");
            out.println();
        }
    }

    static Basket loadFromTxtFile(File textFile) throws IOException {
        try (LineNumberReader in = new LineNumberReader(new FileReader(textFile))) {

            String s;
            String[] names = new String[0];
            long[] prices = new long[0];
            long[] amounts = new long[0];

            while ((s = in.readLine()) != null) {
                if (in.getLineNumber() == 1) {
                    names = s.split(" ");
                }
                if (in.getLineNumber() == 2) {
                    prices = Arrays.stream(s.split(" ")).mapToLong(Integer::parseInt).toArray();
                }

                if (in.getLineNumber() == 3) {
                    amounts = Arrays.stream(s.split(" ")).mapToLong(Integer::parseInt).toArray();
                }
            }
            Basket basket = new Basket(names, prices);
            basket.setAmounts(amounts);
            return basket;
        }
    }

    public void setAmounts(long[] amounts) {
        this.amounts = amounts;
    }

    public void saveBin(File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            Basket basket = new Basket(productNames, prices);
            basket.setAmounts(amounts);
            oos.writeObject(basket);
        }
    }

    public static Object loadFromBinFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return ois.readObject();
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}