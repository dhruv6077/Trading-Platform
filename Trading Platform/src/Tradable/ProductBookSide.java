package Tradable;
import Exceptions.DataValidationException;
import Exceptions.InvalidUserInput;
import Price.Price;
import User.UserManager;

import java.util.*;
import static Tradable.BookSide.*;

public class ProductBookSide {
    private final BookSide side;
    private final HashMap<Price, ArrayList<Tradable>> bookEntries;

    public ProductBookSide(BookSide side) throws InvalidUserInput {
        if(side == null) {
            throw new InvalidUserInput("Bookside cannot be null.");
        }
        this.side = side;
        this.bookEntries = new HashMap<>();
    }

    public TradableDTO add(Tradable o) throws InvalidUserInput {
        if (o == null) {
            throw new InvalidUserInput("Tradable input is null in add method.");
        }
        Price price = o.getPrice();
        ArrayList<Tradable> tAbleArrList = bookEntries.computeIfAbsent(price, k -> new ArrayList<>());
        tAbleArrList.add(o);
        return o.makeTradableDTO();
    }

    public TradableDTO cancel(String tradableId) throws InvalidUserInput {
        if(tradableId == null){
            throw new InvalidUserInput("tradableID is null in cancel method");
        }

        for (ArrayList<Tradable> tradablesArrList : bookEntries.values()) {
            for(Tradable eachTradable : tradablesArrList) { // search the ArrayLists at each price key in the bookEntries HashMap
                if(tradableId.equals(eachTradable.getId())) {
                    tradablesArrList.remove(eachTradable); // remove the tradable from the ArrayList
                    eachTradable.setCancelledVolume(eachTradable.getCancelledVolume() + eachTradable.getRemainingVolume()); // add the tradable’s remaining volume to the tradable’s cancelled volume
                    eachTradable.setRemainingVolume(0);
                    if(!tradablesArrList.isEmpty()){
                        System.out.printf("CANCEL: %s: %s Cxl Qty: %d%n", side, eachTradable.getId(), eachTradable.getCancelledVolume());
                    } else {
                        bookEntries.remove(eachTradable.getPrice()); // remove the price and the empty ArrayList from the bookEntries HashMap
                        System.out.printf("CANCEL: %s: %s Cxl Qty: %d%n", eachTradable.getSide(), eachTradable.getId(), eachTradable.getCancelledVolume());
                    }

                    return eachTradable.makeTradableDTO();

                }

            }

        }
        return null;
    }

    public TradableDTO removeQuotesForUser(String userName) throws InvalidUserInput {
        if(userName == null) {
            throw new InvalidUserInput("Username is null in removeQuotesForUser.");
        }
        for (ArrayList<Tradable> tradablesArrList : bookEntries.values()) { /*search the ArrayLists at each price key in the bookEntries HashMap to find a quote matching the userName passed in*/
            for (Tradable eachTradable : tradablesArrList) {
                if (userName.equals(eachTradable.getUser())) {
                    TradableDTO canceledDTO = cancel(eachTradable.getId());
                    if(tradablesArrList.isEmpty()){
                        bookEntries.remove(eachTradable.getPrice());
                    }

                    if (canceledDTO != null) {
                        return canceledDTO;
                    }
                }

            }
        }
        return null;
    }

    public Price topOfBookPrice() {
        Price highestPrice = null;
        Price lowestPrice = null;

            for (Price price : bookEntries.keySet()) {
                if (lowestPrice == null || price.compareTo(lowestPrice) < 0) {
                    lowestPrice = price;
                }

                if (highestPrice == null || price.compareTo(highestPrice) > 0) {
                    highestPrice = price;
                }
            }
            if(side == BUY) {
                return highestPrice;
            } else {
                return lowestPrice;
            }

    }

    public int topOfBookVolume() {
        int totalVol = 0;
        for (Tradable eachTradable : bookEntries.get(topOfBookPrice())){
            totalVol += eachTradable.getRemainingVolume();
        }
        return totalVol;

    }

    public void tradeOut(Price price, int vol) throws InvalidUserInput, DataValidationException {
        if(price == null) {
            throw new InvalidUserInput("Price is null in tradeOut.");
        }


        int remainingVol = vol;
        ArrayList<Tradable> orderArrList = new ArrayList<>(bookEntries.get(price));

        while (remainingVol > 0) {
            int remVolVal = 0;
            Tradable currOrder = orderArrList.getFirst();
            if (currOrder.getRemainingVolume() <= remainingVol) {
                orderArrList.removeFirst();
                remVolVal = currOrder.getRemainingVolume();
                currOrder.setFilledVolume(currOrder.getFilledVolume() + remVolVal);
                currOrder.setRemainingVolume(0);
                remainingVol -= remVolVal;
                System.out.printf("FULL FILL: (%s %d) %s%n", side, remVolVal, currOrder.toString());
                TradableDTO orderDTO = currOrder.makeTradableDTO();
                UserManager.getInstance().addToUser(currOrder.getUser(), orderDTO);
            } else {

                remVolVal = currOrder.getRemainingVolume();
                currOrder.setRemainingVolume(remVolVal - remainingVol);
                currOrder.setFilledVolume(remVolVal - currOrder.getRemainingVolume());
                System.out.printf("PARTIAL FILL: (%s %d) %s%n", side, remainingVol, currOrder.toString());
                remainingVol = 0;
                TradableDTO orderDTO = currOrder.makeTradableDTO();
                UserManager.getInstance().addToUser(currOrder.getUser(), orderDTO);
            }
        }
        if (orderArrList.isEmpty()) {
            bookEntries.remove(price);
        } else {
            bookEntries.put(price, orderArrList);
        }



    }



    @Override
    public String toString() {
        List<Map.Entry<Price, ArrayList<Tradable>>> bookEntriesList = new ArrayList<Map.Entry<Price, ArrayList<Tradable>>>(bookEntries.entrySet());
        bookEntriesList.sort(new Comparator<Map.Entry<Price, ArrayList<Tradable>>>() {
            @Override
            public int compare(Map.Entry<Price, ArrayList<Tradable>> test1, Map.Entry<Price, ArrayList<Tradable>> test2) {
                return test1.getKey().compareTo(test2.getKey());
            }
        });
        if (side == BUY) {
            Collections.reverse(bookEntriesList);
        }

        System.out.printf("Side: %s%n", side);
        for (Map.Entry<Price, ArrayList<Tradable>> keyValue : bookEntriesList) {
            System.out.printf("    Price: %s%n", keyValue.getKey());
            for (Tradable tAble : keyValue.getValue()) {
                System.out.printf("        %s%n", tAble.toString());
            }
        }
        return "";

    }





}
