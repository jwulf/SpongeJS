package io.github.djxy.spongejs.module.modules;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import io.github.djxy.spongejs.converters.AccountConverter;
import io.github.djxy.spongejs.converters.CurrencyConverter;
import io.github.djxy.spongejs.converters.UniqueAccountConverter;
import io.github.djxy.spongejs.module.Module;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.economy.Currency;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.Account;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by Samuel on 2016-09-07.
 */
public class EconomyModule implements Module {

    private final CurrencyConverter currencyConverter = new CurrencyConverter();

    @Override
    public void initilize(V8 serverRuntime) {
        V8Object economyService = new V8Object(serverRuntime);
        EconomyService service = Sponge.getServiceManager().provide(EconomyService.class).get();

        economyService.add("getDefaultCurrency", new V8Function(serverRuntime, (receiver, parameters) -> currencyConverter.convertToV8(serverRuntime, service.getDefaultCurrency())));
        economyService.add("getCurrencies", new V8Function(serverRuntime, (receiver, parameters) -> {
            V8Array v8Array = new V8Array(serverRuntime);

            for(Currency currency : service.getCurrencies())
                v8Array.push(currencyConverter.convertToV8(serverRuntime, currency));

            return v8Array;
        }));
        economyService.add("getOrCreateAccount", new V8Function(serverRuntime, (receiver, parameters) -> {
            V8Object account = null;
            
            try{
                UUID uuid = UUID.fromString(parameters.getString(0));
                Optional<UniqueAccount> uniqueAccountOpt = service.getOrCreateAccount(uuid);

                if(uniqueAccountOpt.isPresent())
                    account = new UniqueAccountConverter().convertToV8(serverRuntime, uniqueAccountOpt.get());
            } catch (Exception e){
                Optional<Account> accountOpt = service.getOrCreateAccount(parameters.getString(0));

                if(accountOpt.isPresent())
                    account = new AccountConverter().convertToV8(serverRuntime, accountOpt.get());
            }

            return account;
        }));
        economyService.add("hasAccount", new V8Function(serverRuntime, (receiver, parameters) -> {
            try{
                UUID uuid = UUID.fromString(parameters.getString(0));

                return service.hasAccount(uuid);
            } catch (Exception e){
                return service.hasAccount(parameters.getString(0));
            }
        }));

        serverRuntime.add("economyService", economyService);
    }

}
