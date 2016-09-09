package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.TransactionResult;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by Samuel on 2016-09-07.
 */
public class AccountConverter<V extends Account> extends ConverterV8Object<V> {

    private static final CurrencyConverter currencyConverter = new CurrencyConverter();
    private static final BigDecimalConverter bigDecimalConverter = new BigDecimalConverter();
    private static final ContextConverter contextConverter = new ContextConverter();
    private static final TransactionResultConverter transactionResultConverter = new TransactionResultConverter();
    private static final CauseConverter causeConverter = new CauseConverter();
    private static final TextConverter textConverter = new TextConverter();
    private static final AccountConverter accountConverter = new AccountConverter();

    @Override
    public V convertFromV8(Object o) {
        EconomyService service = Sponge.getServiceManager().provide(EconomyService.class).get();
        String identifier = null;

        if(o instanceof String)
            identifier = (String) o;
        else if(o instanceof V8Object){
            if(((V8Object) o).contains("getUniqueID"))
                identifier = ((V8Object) o).executeStringFunction("getUniqueID", new V8Array(((V8Object) o).getRuntime()));
            else
                identifier = ((V8Object) o).executeStringFunction("getIdentifier", new V8Array(((V8Object) o).getRuntime()));
        }

        if(identifier == null)
            throw new IllegalArgumentException("Should be a string or an account.");

        try{
            UUID uuid = UUID.fromString(identifier);
            Optional<UniqueAccount> uniqueAccountOpt = service.getOrCreateAccount(uuid);

            if(uniqueAccountOpt.isPresent())
                return (V) uniqueAccountOpt.get();
        } catch (Exception e){
            Optional<Account> accountOpt = service.getOrCreateAccount(identifier);

            if(accountOpt.isPresent())
                return (V) accountOpt.get();
        }

        return null;
    }

    @Override
    protected V8Object setV8Object(V8Object v8Object, V8 v8, V account) {
        v8Object.add("getIdentifier", new V8Function(v8, (receiver, parameters) -> account.getIdentifier()));
        v8Object.add("deposit", new V8Function(v8, (receiver, parameters) -> {
            TransactionResult transactionResult = null;

            if(parameters.length() == 3)
                transactionResult = account.deposit(currencyConverter.convertFromV8(parameters.get(0)), bigDecimalConverter.convertFromV8(parameters.get(1)), causeConverter.convertFromV8(parameters.get(2)));
            if(parameters.length() == 4)
                transactionResult = account.deposit(currencyConverter.convertFromV8(parameters.get(0)), bigDecimalConverter.convertFromV8(parameters.get(1)), causeConverter.convertFromV8(parameters.get(2)), contextConverter.convertSetFromV8(parameters.get(3)));

            return transactionResult == null?null:transactionResultConverter.convertToV8(v8, transactionResult);
        }));

        v8Object.add("getBalance", new V8Function(v8, (receiver, parameters) -> {
            BigDecimal bigDecimal = null;

            if(parameters.length() == 1)
                bigDecimal = account.getBalance(currencyConverter.convertFromV8(parameters.get(0)));
            if(parameters.length() == 2)
                bigDecimal = account.getBalance(currencyConverter.convertFromV8(parameters.get(0)), contextConverter.convertSetFromV8(parameters.get(1)));

            return bigDecimal != null?bigDecimalConverter.convertToV8(v8, bigDecimal):null;
        }));

        v8Object.add("getBalances", new V8Function(v8, (receiver, parameters) -> {
            Map<Currency,BigDecimal> map = null;

            if(parameters.length() == 0)
                map = account.getBalances();
            if(parameters.length() == 1)
                map = account.getBalances(contextConverter.convertSetFromV8(parameters.get(0)));

            if(map == null || map.isEmpty())
                return new V8Array(v8);

            V8Array v8Array = new V8Array(v8);

            for(Currency currency : map.keySet()){
                V8Object object = new V8Object(v8);

                object.add("currency", currencyConverter.convertToV8(v8, currency));
                object.add("balance", bigDecimalConverter.convertToV8(v8, map.get(currency)));

                v8Array.push(object);
            }

            return v8Array;
        }));

        v8Object.add("getDefaultBalance", new V8Function(v8, (receiver, parameters) -> {
            BigDecimal bigDecimal = null;

            if(parameters.length() == 1)
                bigDecimal = account.getDefaultBalance(currencyConverter.convertFromV8(parameters.get(0)));

            return bigDecimal != null?bigDecimalConverter.convertToV8(v8, bigDecimal):null;
        }));

        v8Object.add("getDisplayName", new V8Function(v8, (receiver, parameters) -> textConverter.convertToV8(v8, account.getDisplayName())));

        v8Object.add("hasBalance", new V8Function(v8, (receiver, parameters) -> {
            if (parameters.length() == 1)
                return account.hasBalance(currencyConverter.convertFromV8(parameters.get(0)));

            return parameters.length() == 2 && account.hasBalance(currencyConverter.convertFromV8(parameters.get(0)), contextConverter.convertSetFromV8(parameters.get(1)));
        }));

        v8Object.add("resetBalance", new V8Function(v8, (receiver, parameters) -> {
            TransactionResult transactionResult = null;

            if(parameters.length() == 2)
                transactionResult = account.resetBalance(currencyConverter.convertFromV8(parameters.get(0)), causeConverter.convertFromV8(parameters.get(1)));
            if(parameters.length() == 3)
                transactionResult = account.resetBalance(currencyConverter.convertFromV8(parameters.get(0)), causeConverter.convertFromV8(parameters.get(1)), contextConverter.convertSetFromV8(parameters.get(2)));

            return transactionResult == null?null:transactionResultConverter.convertToV8(v8, transactionResult);
        }));

        v8Object.add("resetBalances", new V8Function(v8, (receiver, parameters) -> {
            Map<Currency,TransactionResult> map = null;

            if(parameters.length() == 1)
                map = account.resetBalances(causeConverter.convertFromV8(parameters.get(1)));
            if(parameters.length() == 2)
                map = account.resetBalances(causeConverter.convertFromV8(parameters.get(1)), contextConverter.convertSetFromV8(parameters.get(2)));

            if(map == null || map.isEmpty())
                return new V8Object(v8);

            V8Array v8Array = new V8Array(v8);

            for(Currency currency : map.keySet()){
                V8Object object = new V8Object(v8);

                object.add("currency", currencyConverter.convertToV8(v8, currency));
                object.add("transactionResult", transactionResultConverter.convertToV8(v8, map.get(currency)));

                v8Array.push(object);
            }

            return v8Array;
        }));

        v8Object.add("setBalance", new V8Function(v8, (receiver, parameters) -> {
            TransactionResult transactionResult = null;

            if(parameters.length() == 3)
                transactionResult = account.setBalance(currencyConverter.convertFromV8(parameters.get(0)), bigDecimalConverter.convertFromV8(parameters.get(1)), causeConverter.convertFromV8(parameters.get(2)));
            if(parameters.length() == 4)
                transactionResult = account.setBalance(currencyConverter.convertFromV8(parameters.get(0)), bigDecimalConverter.convertFromV8(parameters.get(1)), causeConverter.convertFromV8(parameters.get(2)), contextConverter.convertSetFromV8(parameters.get(3)));

            return transactionResult == null?null:transactionResultConverter.convertToV8(v8, transactionResult);
        }));

        v8Object.add("transfer", new V8Function(v8, (receiver, parameters) -> {
            TransactionResult transactionResult = null;

            if(parameters.length() == 4)
                transactionResult = account.transfer(accountConverter.convertFromV8(parameters.get(0)), currencyConverter.convertFromV8(parameters.get(1)), bigDecimalConverter.convertFromV8(parameters.get(2)), causeConverter.convertFromV8(parameters.get(3)));
            if(parameters.length() == 5)
                transactionResult = account.transfer(accountConverter.convertFromV8(parameters.get(0)), currencyConverter.convertFromV8(parameters.get(1)), bigDecimalConverter.convertFromV8(parameters.get(2)), causeConverter.convertFromV8(parameters.get(3)), contextConverter.convertSetFromV8(parameters.get(4)));

            return transactionResult == null?null:transactionResultConverter.convertToV8(v8, transactionResult);
        }));

        v8Object.add("withdraw", new V8Function(v8, (receiver, parameters) -> {
            TransactionResult transactionResult = null;

            if(parameters.length() == 3)
                transactionResult = account.withdraw(currencyConverter.convertFromV8(parameters.get(0)), bigDecimalConverter.convertFromV8(parameters.get(1)), causeConverter.convertFromV8(parameters.get(2)));
            if(parameters.length() == 4)
                transactionResult = account.withdraw(currencyConverter.convertFromV8(parameters.get(0)), bigDecimalConverter.convertFromV8(parameters.get(1)), causeConverter.convertFromV8(parameters.get(2)), contextConverter.convertSetFromV8(parameters.get(3)));

            return transactionResult == null?null:transactionResultConverter.convertToV8(v8, transactionResult);
        }));

        return v8Object;
    }

}
