package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.*;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Samuel on 2016-09-07.
 */
@ConverterInfo(type = Account.class)
public class AccountConverter extends ConverterV8Object<Account> {

    @Override
    public Account convertFromV8(Object o) {
        EconomyService service = Sponge.getServiceManager().provide(EconomyService.class).get();

        try{
            Account account = Converter.convertFromV8(UniqueAccount.class, o);

            if(account != null)
                return account;
        } catch (IllegalArgumentException e){}

        String identifier = null;

        if(o instanceof String)
            identifier = (String) o;
        else if(o instanceof V8Object)
            if(((V8Object) o).contains("getIdentifier"))
                identifier = ((V8Object) o).executeStringFunction("getIdentifier", null);

        if(identifier == null)
            throw new IllegalArgumentException("Should be a string or an account.");

        Optional<Account> accountOpt = service.getOrCreateAccount(identifier);

        if(accountOpt.isPresent())
            return accountOpt.get();

        return null;
    }

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, Account account, Long uniqueIdentifier) {
        v8Object.add("deposit", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            TransactionResult transactionResult = null;

            try{
                if(parameters.length() == 3)
                    transactionResult = account.deposit(Converter.convertFromV8(Currency.class, parameters.get(0)), Converter.convertFromV8(BigDecimal.class, parameters.get(1)), Converter.convertFromV8(Cause.class, parameters.get(2)));
                if(parameters.length() == 4)
                    transactionResult = account.deposit(Converter.convertFromV8(Currency.class, parameters.get(0)), Converter.convertFromV8(BigDecimal.class, parameters.get(1)), Converter.convertFromV8(Cause.class, parameters.get(2)), Converter.convertSetFromV8(Context.class, parameters.get(3)));

            }catch (Error e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }

            return transactionResult == null?null:Converter.convertToV8(v8, TransactionResult.class, transactionResult);
        }), uniqueIdentifier));

        v8Object.add("getBalance", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            BigDecimal bigDecimal = null;

            if(parameters.length() == 1)
                bigDecimal = account.getBalance(Converter.convertFromV8(Currency.class, parameters.get(0)));
            if(parameters.length() == 2)
                bigDecimal = account.getBalance(Converter.convertFromV8(Currency.class, parameters.get(0)), Converter.convertSetFromV8(Context.class, parameters.get(1)));

            return bigDecimal != null?Converter.convertToV8(v8, BigDecimal.class, bigDecimal):null;
        }), uniqueIdentifier));

        v8Object.add("getBalances", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            Map<Currency,BigDecimal> map = null;

            if(parameters.length() == 0)
                map = account.getBalances();
            if(parameters.length() == 1)
                map = account.getBalances(Converter.convertSetFromV8(Context.class, parameters.get(0)));

            if(map == null || map.isEmpty())
                return new V8Array(v8);

            V8Array v8Array = new V8Array(v8);

            for(Currency currency : map.keySet()){
                V8Object object = new V8Object(v8);

                object.add("currency", (V8Value) Converter.convertToV8(v8, Currency.class, currency));
                object.add("balance", (double) Converter.convertToV8(v8, BigDecimal.class, map.get(currency)));

                v8Array.push(object);
            }

            return v8Array;
        }), uniqueIdentifier));

        v8Object.add("getDefaultBalance", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            BigDecimal bigDecimal = null;

            if(parameters.length() == 1)
                bigDecimal = account.getDefaultBalance(Converter.convertFromV8(Currency.class, parameters.get(0)));

            return bigDecimal != null?Converter.convertToV8(v8, BigDecimal.class, bigDecimal):null;
        }), uniqueIdentifier));

        v8Object.add("getDisplayName", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, Text.class, account.getDisplayName())), uniqueIdentifier));

        v8Object.add("hasBalance", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if (parameters.length() == 1)
                return account.hasBalance(Converter.convertFromV8(Currency.class, parameters.get(0)));

            return parameters.length() == 2 && account.hasBalance(Converter.convertFromV8(Currency.class, parameters.get(0)), Converter.convertSetFromV8(Context.class, parameters.get(1)));
        }), uniqueIdentifier));

        v8Object.add("resetBalance", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            TransactionResult transactionResult = null;

            if(parameters.length() == 2)
                transactionResult = account.resetBalance(Converter.convertFromV8(Currency.class, parameters.get(0)), Converter.convertFromV8(Cause.class, parameters.get(1)));
            if(parameters.length() == 3)
                transactionResult = account.resetBalance(Converter.convertFromV8(Currency.class, parameters.get(0)), Converter.convertFromV8(Cause.class, parameters.get(1)), Converter.convertSetFromV8(Context.class, parameters.get(2)));

            return transactionResult == null?null:Converter.convertToV8(v8, TransactionResult.class, transactionResult);
        }), uniqueIdentifier));

        v8Object.add("resetBalances", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            Map<Currency,TransactionResult> map = null;

            if(parameters.length() == 1)
                map = account.resetBalances(Converter.convertFromV8(Cause.class, parameters.get(1)));
            if(parameters.length() == 2)
                map = account.resetBalances(Converter.convertFromV8(Cause.class, parameters.get(1)), Converter.convertSetFromV8(Context.class, parameters.get(2)));

            if(map == null || map.isEmpty())
                return new V8Array(v8);

            V8Array v8Array = new V8Array(v8);

            for(Currency currency : map.keySet()){
                V8Object object = new V8Object(v8);

                object.add("currency", (V8Value) Converter.convertToV8(v8, Currency.class, currency));
                object.add("transactionResult", (V8Value) Converter.convertToV8(v8, TransactionResult.class, map.get(currency)));

                v8Array.push(object);
            }

            return v8Array;
        }), uniqueIdentifier));

        v8Object.add("setBalance", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            TransactionResult transactionResult = null;

            if(parameters.length() == 3)
                transactionResult = account.setBalance(Converter.convertFromV8(Currency.class, parameters.get(0)), Converter.convertFromV8(BigDecimal.class, parameters.get(1)), Converter.convertFromV8(Cause.class, parameters.get(2)));
            if(parameters.length() == 4)
                transactionResult = account.setBalance(Converter.convertFromV8(Currency.class, parameters.get(0)), Converter.convertFromV8(BigDecimal.class, parameters.get(1)), Converter.convertFromV8(Cause.class, parameters.get(2)), Converter.convertSetFromV8(Context.class, parameters.get(3)));

            return transactionResult == null?null:Converter.convertToV8(v8, TransactionResult.class, transactionResult);
        }), uniqueIdentifier));

        v8Object.add("transfer", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            TransactionResult transactionResult = null;

            if(parameters.length() == 4)
                transactionResult = account.transfer(convertFromV8(parameters.get(0)), Converter.convertFromV8(Currency.class, parameters.get(1)), Converter.convertFromV8(BigDecimal.class, parameters.get(2)), Converter.convertFromV8(Cause.class, parameters.get(3)));
            if(parameters.length() == 5)
                transactionResult = account.transfer(convertFromV8(parameters.get(0)), Converter.convertFromV8(Currency.class, parameters.get(1)), Converter.convertFromV8(BigDecimal.class, parameters.get(2)), Converter.convertFromV8(Cause.class, parameters.get(3)), Converter.convertSetFromV8(Context.class, parameters.get(4)));

            return transactionResult == null?null:Converter.convertToV8(v8, TransactionResult.class, transactionResult);
        }), uniqueIdentifier));

        v8Object.add("withdraw", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            TransactionResult transactionResult = null;

            if(parameters.length() == 3)
                transactionResult = account.withdraw(Converter.convertFromV8(Currency.class, parameters.get(0)), Converter.convertFromV8(BigDecimal.class, parameters.get(1)), Converter.convertFromV8(Cause.class, parameters.get(2)));
            if(parameters.length() == 4)
                transactionResult = account.withdraw(Converter.convertFromV8(Currency.class, parameters.get(0)), Converter.convertFromV8(BigDecimal.class, parameters.get(1)), Converter.convertFromV8(Cause.class, parameters.get(2)), Converter.convertSetFromV8(Context.class, parameters.get(3)));

            return transactionResult == null?null:Converter.convertToV8(v8, TransactionResult.class, transactionResult);
        }), uniqueIdentifier));
    }

}
