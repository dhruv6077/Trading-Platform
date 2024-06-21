package User;

import Exceptions.DataValidationException;
import Exceptions.InvalidPriceOperation;
import Exceptions.InvalidUserInput;
import Tradable.*;

import java.util.*;

import static Tradable.BookSide.*;

public final class ProductManager {
    private static ProductManager instance;
    private final Map<String, ProductBook> pbooks = new HashMap<>();
    private static final String symbolRegex = "[A-Z]{1,5}";

    private ProductManager() {
    }

    public static ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    public void addProduct(String symbol) throws InvalidUserInput, DataValidationException {
        if (symbol == null || !symbol.matches(symbolRegex)) {
            throw new DataValidationException("Symbol entered is invalid");
        }
        pbooks.put(symbol, new ProductBook(symbol));
    }

    public ProductBook getProductBook(String symbol) throws DataValidationException {
        if (symbol == null || !symbol.matches(symbolRegex)) {
            throw new DataValidationException("Symbol entered is invalid.");
        }

        ProductBook pb = pbooks.get(symbol); 
        if (pb == null) {
            throw new DataValidationException("Product is not found in ProductBook.");
        }
        return pb;
    }

    public String getRandomProduct() throws DataValidationException {
        if (pbooks.isEmpty()) {
            throw new DataValidationException("ProductBook is empty.");
        }

        List<String> listOfSymbols = new ArrayList<>(pbooks.keySet());
        Collections.shuffle(listOfSymbols);
        return listOfSymbols.getFirst();
    }

    public TradableDTO addTradable(Tradable o) throws DataValidationException, InvalidUserInput, InvalidPriceOperation {
        if (o == null) {
            throw new DataValidationException("Tradable entered is null");
        }

        TradableDTO trdbleDTO = getProductBook(o.getProduct()).add(o);
        TradableDTO mketrdbleDTO = o.makeTradableDTO();
        UserManager.getInstance().addToUser(o.getUser(), mketrdbleDTO);

        return trdbleDTO;
    }


    public TradableDTO[] addQuote(Quote q) throws DataValidationException, InvalidUserInput, InvalidPriceOperation {
        if (q == null) {
            throw new DataValidationException("Quote entered is null");
        }

        getProductBook(q.getSymbol()).removeQuotesForUser(q.getUser());
        TradableDTO buyTradeDTO = addTradable(q.getQuoteSide(BUY));
        TradableDTO sellTradeDTO = addTradable(q.getQuoteSide(SELL));
        return new TradableDTO[] {buyTradeDTO, sellTradeDTO};
    }

    public TradableDTO cancel(TradableDTO o) throws DataValidationException, InvalidUserInput, InvalidPriceOperation {
        if (o == null) {
            throw new DataValidationException("TradableDTO entered is null");
        }

        TradableDTO canceledTradableDTO = getProductBook(o.product).cancel(o.side, o.id);

        if (canceledTradableDTO == null) {
            System.out.println("failure to cancel");
            return null;
        }

        return canceledTradableDTO;
    }

    public TradableDTO[] cancelQuote(String symbol, String user) throws DataValidationException, InvalidUserInput, InvalidPriceOperation {
        if (symbol == null || user == null) {
            throw new DataValidationException("Symbol or user cannot be null");
        }

        return getProductBook(symbol).removeQuotesForUser(user);
    }

    public ArrayList<String> getProductList() {
        return new ArrayList<>(pbooks.keySet());
    }

    @Override
    public String toString() {
        for (ProductBook pb : pbooks.values()) {
            System.out.println(pb.toString());
        }

        return "";
    }
}