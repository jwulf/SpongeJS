package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import io.github.djxy.spongejs.converter.Converter;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.text.Text;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by Samuel on 2016-09-07.
 */
@ConverterInfo(type = Currency.class)
public class CurrencyConverter extends ConverterV8Object<Currency> {

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, Currency currency, UUID uniqueIdentifier) {
        v8Object.add("format", registerV8Function(new V8Function(v8, (receiver, parameters) -> {
            if(parameters.length() == 1)
                return Converter.convertToV8(v8, Text.class, currency.format(Converter.convertFromV8(BigDecimal.class, parameters.get(0))));
            else if(parameters.length() == 2)
                return Converter.convertToV8(v8, Text.class, currency.format(Converter.convertFromV8(BigDecimal.class, parameters.get(0)), parameters.getInteger(1)));
            else
                return null;
        }), uniqueIdentifier));
        v8Object.add("getDefaultFractionDigits", registerV8Function(new V8Function(v8, (receiver, parameters) -> currency.getDefaultFractionDigits()), uniqueIdentifier));
        v8Object.add("getDisplayName", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, Text.class, currency.getDisplayName())), uniqueIdentifier));
        v8Object.add("getPluralDisplayName", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, Text.class, currency.getPluralDisplayName())), uniqueIdentifier));
        v8Object.add("getSymbol", registerV8Function(new V8Function(v8, (receiver, parameters) -> Converter.convertToV8(v8, Text.class, currency.getSymbol())), uniqueIdentifier));
        v8Object.add("isDefault", registerV8Function(new V8Function(v8, (receiver, parameters) -> currency.isDefault()), uniqueIdentifier));
    }

    @Override
    public Currency convertFromV8(Object o) {
        String currencyId = null;

        if(o instanceof String)
            currencyId = (String) o;
        else if(o instanceof V8Object)
            currencyId = ((V8Object) o).executeStringFunction("getId", null);

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
