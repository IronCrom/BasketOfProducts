import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, ParserConfigurationException, SAXException {
        Scanner scanner = new Scanner(System.in);
        ClientLog clientLog = new ClientLog();
        GsonBuilder builderGson = new GsonBuilder();
        Gson gson = builderGson.create();

//        File basketFile = new File("basket.json");
//        File logFile = new File("log.csv");
        File xmlFile = new File("shop.xml");
        Config load = new Config().config(xmlFile, Config.config.load);
        Config save = new Config().config(xmlFile, Config.config.save);
        Config log = new Config().config(xmlFile, Config.config.log);

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

        if (load.isEnabled()) {
            File loadFile = new File(load.getFileName());
            if (load.getFormat().equals(Config.Format.json)) {
                basket = gson.fromJson(new FileReader(loadFile), Basket.class);
            }
            if (load.getFormat().equals(Config.Format.text)) {
                basket = Basket.loadFromTxtFile(loadFile);
            }
            //basket = Basket.loadFromTxtFile(basketFile);
            //basket = (Basket) Basket.loadFromBinFile(basketFile);
            //basket = gson.fromJson(new FileReader(basketFile), Basket.class);
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
                if (log.isEnabled()) {
                    clientLog.exportAsCSV(new File(log.getFileName()));
                }
                break;
            } else {
                //clientLog.log(input); //реализация лога через сохранение введённой строки
                String[] parts = input.split(" ");
                int productNumber = Integer.parseInt(parts[0]) - 1;
                int productCount = Integer.parseInt(parts[1]);
                clientLog.log(productNumber + 1, productCount);
                if (basket.getPrices()[productNumber] == 0) {
                    prices[productNumber] = products[productNumber].getPrice();
                    productNames[productNumber] = products[productNumber].getName();
                    basket.setPrices(prices);
                    basket.setProductNames(productNames);
                }
                basket.addToCart(productNumber, productCount);

                if (save.isEnabled()) {
                    File saveFile = new File(save.getFileName());
                    if (save.getFormat().equals(Config.Format.json)) {
                        try (FileWriter file = new FileWriter(saveFile)) {
                            file.write(gson.toJson(basket));
                        }
                    }
                    if (save.getFormat().equals(Config.Format.text)) {
                        basket.saveTxt(saveFile);
                    }
                }
                //basket.saveBin(basketFile);
            }
        }
    }
}
