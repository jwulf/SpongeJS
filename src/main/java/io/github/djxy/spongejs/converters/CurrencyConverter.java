package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;

/**
 * Created by Samuel on 2016-09-07.
 */
@ConverterInfo(type = Currency.class)
public class CurrencyConverter extends ConverterV8Object<Currency> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, Currency currency) {
        v8Object.add("format", new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() == 1)
                return Converter.convertToV8(v8, Text.class, currency.format(Converter.convertFromV8(BigDecimal.class, parameters.get(0))));
            else if(parameters.length() == 2)
                return Converter.convertToV8(v8, Text.class, currency.format(Converter.convertFromV8(BigDecimal.class, parameters.get(0)), parameters.getInteger(1)));
            else
                return null;
        }));
        v8Object.add("getDefaultFractionDigits", new V8Function(v8, (receiver, parameters) -> currency.getDefaultFractionDigits()));
        v8Object.add("getDisplayName", new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, Text.class, currency.getDisplayName())));
        v8Object.add("getPluralDisplayName", new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, Text.class, currency.getPluralDisplayName())));
        v8Object.add("getSymbol", new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, Text.class, currency.getSymbol())));
        v8Object.add("isDefault", new V8Function(v8, (receiver, parameters) -> currency.isDefault()));
    }

    @Override
    public Currency convertFromV8(Object o) {
        String currencyId = null;

        if(o instanceof String)
            currencyId = (String) o;
        else if(o instanceof V8Object)
            currencyId = ((V8Object) o).executeStringFunction("getId", new V8Array(((V8Object) o).getRuntime()));

        if(currencyId == null)
            throw new IllegalArgumentException("Should be a string or a currency object.");

        EconomyService economyService = Sponge.getServiceManager().provide(EconomyService.class).get();

        if(economyService.getDefaultCurrency().getId().equals(currencyId))
            return economyService.getDefaultCurrency();

        for (Currency currency : economyService.getCurrencies())
            if (currency.getId().equals(currencyId))
                return currency;

        return null;
    }

}
