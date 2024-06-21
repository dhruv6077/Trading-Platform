package Tradable;

import Exceptions.DataValidationException;
import Exceptions.InvalidPriceOperation;
import Exceptions.InvalidUserInput;
import Market.CurrentMarketTracker;
import Price.*;

import static Tradable.BookSide.*;
import User.UserManager;

public class ProductBook {
    private final String product;
    ProductBookSide buySide;
    ProductBookSide sellSide;
    private static final String productIdRegex = "[a-zA-Z0-9.]*";

    public ProductBook(String product) throws InvalidUserInput {
        if (product == null || product.isEmpty() || product.length() > 5 || !product.matches(productIdRegex)) {
            throw new InvalidUserInput("Invalid product input.");
        }
        this.product = product;
        this.buySide = new ProductBookSide(BUY);
        this.sellSide = new ProductBookSide(SELL);
    }

    public TradableDTO add(Tradable t) throws InvalidUserInput, DataValidationException, InvalidPriceOperation {
        if(t == null) {
            throw new InvalidUserInput("Tradable cannot be null.");
        }

        ProductBookSide side;
        if (t.getSide() == BUY) {
            System.out.printf("ADD: %s: %s%n", BUY, t);
            side = buySide;
        } else {
            System.out.printf("ADD: %s: %s%n", SELL, t);
            side = sellSide;
        }
        TradableDTO addDTO = side.add(t);
        tryTrade();
        updateMarket();
        return addDTO;

    }

    public TradableDTO[] add(Quote qte) throws InvalidUserInput, DataValidationException, InvalidPriceOperation {
        TradableDTO[] arrOfDtos = new TradableDTO[2];
        arrOfDtos[0] = buySide.add(qte.getQuoteSide(BUY));
        System.out.printf("ADD: %s: %s%n", BUY, qte.getQuoteSide(BUY));
        arrOfDtos[1] = sellSide.add(qte.getQuoteSide(SELL));
        System.out.printf("ADD: %s: %s%n", SELL, qte.getQuoteSide(SELL));
        tryTrade();
        updateMarket();
        return arrOfDtos;
    }

    public TradableDTO cancel(BookSide side, String orderId) throws InvalidUserInput, InvalidPriceOperation {
        TradableDTO sides;
        if(side == BUY) {
            sides = buySide.cancel(orderId);

        } else {
            sides =  sellSide.cancel(orderId);

        }
        updateMarket();
        return sides;


    }

    public void tryTrade() throws InvalidUserInput, DataValidationException {
        Price topBuyPrice = buySide.topOfBookPrice();      // Get Top Buy Price
        Price topSellPrice = sellSide.topOfBookPrice();    // Get Top Sell Price

        while (topBuyPrice != null && topSellPrice != null && topBuyPrice.compareTo(topSellPrice) >= 0) {
            int topBuyVolume = buySide.topOfBookVolume();       // Get Top Buy Volume
            int topSellVolume = sellSide.topOfBookVolume();     // Get Top Sell Volume
            int volumeToTrade = Math.min(topBuyVolume, topSellVolume);      // Volume to trade is the MIN of the Buy and Sell volumes

            sellSide.tradeOut(topSellPrice, volumeToTrade);

            buySide.tradeOut(topBuyPrice, volumeToTrade);

            topBuyPrice = buySide.topOfBookPrice();     // Get Top Buy Price
            topSellPrice = sellSide.topOfBookPrice();   // Get Top Sell Price
        }
    }

    public TradableDTO[] removeQuotesForUser(String userName) throws InvalidUserInput, InvalidPriceOperation {
        TradableDTO[] arrayOfDTO = new TradableDTO[2];
        arrayOfDTO[0] = buySide.removeQuotesForUser(userName);
        arrayOfDTO[1] = sellSide.removeQuotesForUser(userName);

        UserManager.getInstance().getUser(userName).addTradable(arrayOfDTO[0]);
        UserManager.getInstance().getUser(userName).addTradable(arrayOfDTO[1]);
        updateMarket();


        return arrayOfDTO;
    }

    private void updateMarket() throws InvalidPriceOperation {
        int topSellVolume;
        int topBuyVolume;
        Price topBuyPrice = buySide.topOfBookPrice();
        Price topSellPrice = sellSide.topOfBookPrice();

        if(topBuyPrice == null && topSellPrice != null) {
            CurrentMarketTracker.getInstance().updateMarket(product, topBuyPrice, 0, topSellPrice, sellSide.topOfBookVolume());
        } else if (topSellPrice == null && topBuyPrice != null) {
            CurrentMarketTracker.getInstance().updateMarket(product, topBuyPrice, buySide.topOfBookVolume(), topSellPrice, 0);
        }

        if (topBuyPrice != null && topSellPrice != null) {
            topBuyVolume = buySide.topOfBookVolume();
            topSellVolume = sellSide.topOfBookVolume();

            CurrentMarketTracker.getInstance().updateMarket(product, topBuyPrice, topBuyVolume, topSellPrice, topSellVolume);
        }

        if(topBuyPrice == null && topSellPrice == null) {
            CurrentMarketTracker.getInstance().updateMarket(product, topBuyPrice, 0, topSellPrice, 0);
        }




    }


        @Override
        public String toString() {
            System.out.println("-----------------------------------");
            System.out.printf("Product Book: %s%n", product);
            return "%s\n%s\n-----------------------------------".formatted(buySide.toString(), sellSide.toString());
        }



}
