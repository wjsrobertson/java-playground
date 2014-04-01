package net.xylophones.concurrency.stockserver;

import com.google.common.collect.Maps;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class Server {

    class Stock {
        String ticker;
        BigDecimal price;
        Integer numShares;

        Stock(String ticker, BigDecimal price, Integer numShares) {
            this.ticker = ticker;
            this.price = price;
            this.numShares = numShares;
        }

        public int hashCode() {
            return ticker.hashCode();
        }
        public boolean equals(Stock other) {
            return other.ticker.equals(this.ticker);
        }
    }

    class Portfolio {
        Map<Stock,Integer> stocks = Maps.newHashMap();
    }

    class StockValueEstimate {
        BigDecimal value;
        BigDecimal error;
    }

    class Trader {
    }

    class ValueTrader extends Trader {
        int valueAccuracy;  // 1 - 10
        List<StockValueEstimate> stockValueEstimates;
        Portfolio portfolio = new Portfolio();
    }

    class MomentumTrader {
        int lookbackTimeframe;
        int closePositionTimeframe;
    }

    class NewsSource {

    }

    public static void main(String[] args) {

    }

}
