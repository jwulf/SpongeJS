package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.transaction.TransactionResult;

import java.math.BigDecimal;

/**
 * Created by Samuel on 2016-09-07.
 */
public class AccountConverter<V extends Account> extends ConverterV8Object<V> {

    private final CurrencyConverter currencyConverter = new CurrencyConverter();
    private final BigDecimalConverter bigDecimalConverter = new BigDecimalConverter();
    private final ContextConverter contextConverter = new ContextConverter();
    private final TransactionResultConverter transactionResultConverter = new TransactionResultConverter();
    private final CauseConverter causeConverter = new CauseConverter();

    @Override
    protected V8Object setV8Object(V8Object v8Object, V8 v8, V account) {
        v8Object.add("deposit", new V8Function(v8, (receiver, parameters) -> {
            TransactionResult transactionResult = null;
            if(parameters.length() == 3)
                transactionResult = account.deposit(currencyConverter.convertFromV8(parameters.get(0)), bigDecimalConverter.convertFromV8(parameters.get(1)), causeConverter.convertFromV8(parameters.get(2)));
            if(parameters.length() == 4)
                transactionResult = account.deposit(currencyConverter.convertFromV8(parameters.get(0)), bigDecimalConverter.convertFromV8(parameters.get(1)), causeConverter.convertFromV8(parameters.get(2)), contextConverter.convertSetFromV8(parameters.get(2)));

            return transactionResult == null?null:transactionResultConverter.convertToV8(v8, transactionResult);
        }));

        v8Object.add("getBalance", new V8Function(v8, (receiver, parameters) -> {
            BigDecimal bigDecimal = null;

            if(parameters.length() == 1)
                bigDecimal = account.getBalance(currencyConverter.convertFromV8(parameters.get(0)));
            if(parameters.length() == 2)
                bigDecimal = account.getBalance(currencyConverter.convertFromV8(parameters.get(0)), contextConverter.convertSetFromV8(parameters.get(1)));

            if(bigDecimal != null)
                return bigDecimalConverter.convertToV8(v8, bigDecimal);

            return null;
        }));

        v8Object.add("getBalances", new V8Function(v8, (receiver, parameters) -> {

            return null;
        }));

        v8Object.add("getDefaultBalance", new V8Function(v8, (receiver, parameters) -> {

            return null;
        }));

        v8Object.add("getDisplayName", new V8Function(v8, (receiver, parameters) -> {

            return null;
        }));

        v8Object.add("hasBalance", new V8Function(v8, (receiver, parameters) -> {

            return null;
        }));

        v8Object.add("resetBalance", new V8Function(v8, (receiver, parameters) -> {

            return null;
        }));

        v8Object.add("resetBalances", new V8Function(v8, (receiver, parameters) -> {

            return null;
        }));

        v8Object.add("setBalance", new V8Function(v8, (receiver, parameters) -> {

            return null;
        }));

        v8Object.add("transfer", new V8Function(v8, (receiver, parameters) -> {

            return null;
        }));

        v8Object.add("withdraw", new V8Function(v8, (receiver, parameters) -> {

            return null;
        }));

        return v8Object;
    }

}
