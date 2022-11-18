import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ClientLog {

    private final Collection<ClientLog> clientLogCollection = new ArrayList<>();
    private int productNum;
    private int amount;

    public ClientLog(int productNum, int amount) {
        this.productNum = productNum;
        this.amount = amount;
    }

    public ClientLog() {

    }
//реализация лога через сохранение введённой строки
    //private ArrayList<String> clientLog;

//    public ClientLog(ArrayList<String> clientLog) {
//        this.clientLog = clientLog;
//    }

//    public void log(String input) {
//        clientLog.add(input.replace(' ', ','));
//    }

    public void log(int productNum, int amount) {
        ClientLog clientLog = new ClientLog(productNum, amount);
        clientLogCollection.add(clientLog);
    }

//реализация лога через сохранение введённой строки

//    public void exportAsCSV(File txtFile) throws IOException {
//        try (CSVWriter writer = new CSVWriter(new FileWriter(txtFile),',', CSVWriter.NO_QUOTE_CHARACTER)){
//            for( String str : clientLog) {
//                String[] logStr = str.toString().split(",");
//                writer.writeNext(logStr);
//            }
//        }
//    }

    public void exportAsCSV(File txtFile) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        ColumnPositionMappingStrategy<ClientLog> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(ClientLog.class);
        strategy.setColumnMapping("productNum", "amount");

        try (ICSVWriter writer = new CSVWriterBuilder(new FileWriter(txtFile))
                .build()) {

            writer.writeAll(Collections.singletonList(strategy.getColumnMapping()), false);

            StatefulBeanToCsv<ClientLog> sbc = new StatefulBeanToCsvBuilder<ClientLog>(writer)
                    .withApplyQuotesToAll(false)
                    .withMappingStrategy(strategy)
                    .build();

            sbc.write(clientLogCollection.iterator());
        }
    }
}

