package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.service.economy.transaction.TransactionResult;

/**
 * Created by Samuel on 2016-09-07.
 */
public class TransactionResultConverter extends ConverterV8Object<TransactionResult> {

    @Override
    protected V8Object setV8Object(V8Object v8Object, V8 v8, TransactionResult transactionResult) {
        v8Object.add("getAccount", new V8Function(v8, (receiver, parameters) -> {
            if(transactionResult.getAccount() instanceof UniqueAccount)
                return new UniqueAccountConverter().convertToV8(v8, (UniqueAccount) transactionResult.getAccount());

            return new AccountConverter<>().convertToV8(v8, transactionResult.getAccount());
        }));

        v8Object.add("getAmount", new V8Function(v8, (receiver, parameters) -> new BigDecimalConverter().convertToV8(v8, transactionResult.getAmount())));

        v8Object.add("getContexts", new V8Function(v8, (receiver, parameters) -> new ContextConverter().convertSetToV8(v8, transactionResult.getContexts())));

        v8Object.add("getCurrency", new V8Function(v8, (receiver, parameters) -> new CurrencyConverter().convertToV8(v8, transactionResult.getCurrency())));

        v8Object.add("getResult", new V8Function(v8, (receiver, parameters) -> new ResultTypeConverter().convertToV8(v8, transactionResult.getResult())));

        v8Object.add("getType", new V8Function(v8, (receiver, parameters) -> new CatalogTypeConverter<>().convertToV8(v8, transactionResult.getType())));

        return v8Object;
    }

}
