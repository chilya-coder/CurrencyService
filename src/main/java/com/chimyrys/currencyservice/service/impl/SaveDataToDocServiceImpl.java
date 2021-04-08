package com.chimyrys.currencyservice.service.impl;

import com.chimyrys.currencyservice.model.Currency;
import com.chimyrys.currencyservice.model.ExchangeRate;
import com.chimyrys.currencyservice.service.api.CurrencyService;
import com.chimyrys.currencyservice.service.api.SaveDataToDocService;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SaveDataToDocServiceImpl implements SaveDataToDocService {
    private final static Logger logger = Logger.getLogger(SaveDataToDocServiceImpl.class);
    private final List<CurrencyService> currencyServices;
    private final File template;

    public SaveDataToDocServiceImpl(List<CurrencyService> currencyServices) {
        template = new File("template.docx");
        this.currencyServices = currencyServices;
    }

    /**
     * Method that saves bank info into doc template file
     * @param currencyFrom
     * @param currencyTo
     * @param date
     * @return
     */
    @Override
    public byte[] saveExchangeRate(Currency currencyFrom, Currency currencyTo, LocalDateTime date, CurrencyService currencyService) {
        ExchangeRate exchangeRate = currencyService.getCurrency(date, currencyFrom, currencyTo);
        try {
            logger.debug("Filling info to doc");
            XWPFDocument document = new XWPFDocument(OPCPackage.open(template));
            for (XWPFParagraph paragraph: document.getParagraphs()) {
                for (XWPFRun run: paragraph.getRuns()) {
                    String text = run.getText(0);
                    text = text.replace("API", currencyService.getName());
                    text = text.replace("CURFROM", currencyFrom.getValue());
                    text = text.replace("CURTO", currencyTo.getValue());
                    text = text.replace("BUY", Float.toString(exchangeRate.getBuyRate()));
                    text = text.replace("SELL", Float.toString(exchangeRate.getSellRate()));
                    text = text.replace("DATE", exchangeRate.getDate().toString());
                    run.setText(text,0);
                }
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            document.write(stream);
            return stream.toByteArray();
        } catch (IOException | InvalidFormatException e) {
            logger.error("Can't save data to doc file");
            e.printStackTrace();
        }
        return null;
    }
}
