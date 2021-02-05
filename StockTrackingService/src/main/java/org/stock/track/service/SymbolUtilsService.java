package org.stock.track.service;

import java.math.BigDecimal;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.stock.track.pojo.CurrentProgress;
import org.stock.track.pojo.CurrentStockValueResponse;

@Service
public class SymbolUtilsService {
    @Value("${symbolLabelFilter}")
    private String symbolLabelFilter;

    @Value("${typeFilter}")
    private String typeFilter;

    public CurrentStockValueResponse createSymbolResponseObject(String symbolName, BigDecimal lastPrice, Long timestamp,
            CurrentProgress progress) {
        String symbol = this.getReplacedSymbolname(symbolName);
        String type = this.getSymbolType(symbolName);
        return new CurrentStockValueResponse(symbol, symbolName, lastPrice, timestamp, progress, type);
    }

    private String getReplacedSymbolname(String symbol) {
        JSONObject jObject = new JSONObject(this.symbolLabelFilter);
        Iterator<?> keys = jObject.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String value = jObject.getString(key);
            symbol = symbol.replace(key, value.toString());
        }
        return symbol;
    }

    private String getSymbolType(String symbol) {
        String type;
        type = "STOCK";
        JSONArray jArr = new JSONArray(this.typeFilter);
        for (int i = 0; i < jArr.length(); i++) {
            JSONObject jObj = jArr.getJSONObject(i);
            JSONArray jArrValues = jObj.getJSONArray("values");
            for (int j = 0; j < jArrValues.length(); j++) {
                if (symbol.contains(jArrValues.get(j).toString())) {
                    return jObj.getString("type");
                }
            }
        }
        return type;
    }
}