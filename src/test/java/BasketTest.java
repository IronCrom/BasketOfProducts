import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class BasketTest {
    static Basket basket = new Basket(new String[]{"Milk", "Juice", "IceCream"},new long[]{10,15,20});

    @BeforeAll
    public static void beforeAll(){
        //Basket basket = new Basket(new String[]{"Milk", "Juice", "IceCream"},new long[]{10,15,20});
        Random random = new Random();
        long[] amounts = new long[3];
        for(int i = 0; i < amounts.length; i++) {
            amounts[i] = random.nextInt(10);
        }
        basket.setAmounts(amounts);
    }

    @Test
    public void testPrintCart() {
        Assertions.assertNotNull(basket);
        basket.printCart();
    }

    @Test
    public void testSaveTxt() throws IOException {
        basket.saveTxt(new File("testTxt.txt"));
    }

    @Test
    public void testLoadFromTxtFile() throws IOException {
        Basket basket1 = Basket.loadFromTxtFile(new File("testTxt.txt"));
        Assertions.assertArrayEquals(basket1.getPrices(), basket.getPrices());
        Assertions.assertArrayEquals(basket1.getProductNames(), basket.getProductNames());
        Assertions.assertArrayEquals(basket1.getAmounts(), basket.getAmounts());
    }
}
