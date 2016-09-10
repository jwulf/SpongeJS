package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.ResultType;
import org.spongepowered.api.service.economy.transaction.TransactionResult;
import org.spongepowered.api.service.economy.transaction.TransactionType;

import java.math.BigDecimal;

/**
 * Created by Samuel on 2016-09-07.
 */
@ConverterInfo(type = TransactionResult.class)
public class TransactionResultConverter extends ConverterV8Object<TransactionResult> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, TransactionResult transactionResult) {
        v8Object.add("getAccount", new V8Function(v8, (receiver, parameters) -> {
            if(transactionResult.getAccount() instanceof UniqueAccount)
                return Converter.convertToV8(v8, UniqueAccount.class, (UniqueAccount) transactionResult.getAccount());

            return Converter.convertToV8(v8, Account.class, transactionResult.getAccount());
        }));

        v8Object.add("getAmount", new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, BigDecimal.class, transactionResult.getAmount())));

        v8Object.add("getContexts", new V8Function(v8, (receiver, parameters) -> Converter.convertSetToV8(v8, Context.class, transactionResult.getContexts())));

        v8Object.add("getCurrency", new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, Currency.class, transactionResult.getCurrency())));

        v8Object.add("getResult", new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, ResultType.class, transactionResult.getResult())));

        v8Object.add("getType", new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, TransactionType.class, transactionResult.getType())));
    }

}
