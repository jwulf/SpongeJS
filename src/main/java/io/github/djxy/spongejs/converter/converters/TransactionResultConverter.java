package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
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
    public void setV8Object(V8Object v8Object, V8 v8, TransactionResult transactionResult, Long uniqueIdentifier) {
        v8Object.add("getAccount", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if(transactionResult.getAccount() instanceof UniqueAccount)
                return Converter.convertToV8(v8, UniqueAccount.class, (UniqueAccount) transactionResult.getAccount());

            return Converter.convertToV8(v8, Account.class, transactionResult.getAccount());
        }), uniqueIdentifier));

        v8Object.add("getAmount", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, BigDecimal.class, transactionResult.getAmount())), uniqueIdentifier));

        v8Object.add("getContexts", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertIterableToV8(v8, Context.class, transactionResult.getContexts())), uniqueIdentifier));

        v8Object.add("getCurrency", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, Currency.class, transactionResult.getCurrency())), uniqueIdentifier));

        v8Object.add("getResult", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, ResultType.class, transactionResult.getResult())), uniqueIdentifier));

        v8Object.add("getType", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, TransactionType.class, transactionResult.getType())), uniqueIdentifier));
    }

}
