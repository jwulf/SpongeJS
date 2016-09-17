package io.github.djxy.spongejs.module.modules;

import com.eclipsesource.v8.*;
import io.github.djxy.spongejs.converter.Converter;
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

    @Override
    public void initialize(V8 serverRuntime) {
        V8Object economyModule = new V8Object(serverRuntime);
        Optional<EconomyService> service = Sponge.getServiceManager().provide(EconomyService.class);

        if(!service.isPresent())
            return;

        EconomyService economyService = service.get();

        economyModule.add("getDefaultCurrency", new V8Function(serverRuntime, (receiver, parameters) -> Converter.convertToV8(serverRuntime, Currency.class, economyService.getDefaultCurrency())));
        economyModule.add("getCurrencies", new V8Function(serverRuntime, (receiver, parameters) -> {
            V8Array v8Array = new V8Array(serverRuntime);

            for (Currency currency : economyService.getCurrencies())
                v8Array.push((V8Value) Converter.convertToV8(serverRuntime, Currency.class, currency));

            return v8Array;
        }));
        economyModule.add("getOrCreateAccount", new V8Function(serverRuntime, (receiver, parameters) -> {
            Object account = null;

            try {
                UUID uuid = UUID.fromString(parameters.getString(0));
                Optional<UniqueAccount> uniqueAccountOpt = economyService.getOrCreateAccount(uuid);

                if (uniqueAccountOpt.isPresent())
                    account = Converter.convertToV8(serverRuntime, UniqueAccount.class, uniqueAccountOpt.get());
            } catch (Exception e) {
                Optional<Account> accountOpt = economyService.getOrCreateAccount(parameters.getString(0));

                if (accountOpt.isPresent())
                    account = Converter.convertToV8(serverRuntime, Account.class, accountOpt.get());
            }

            return account;
        }));
        economyModule.add("hasAccount", new V8Function(serverRuntime, (receiver, parameters) -> {
            try {
                UUID uuid = UUID.fromString(parameters.getString(0));

                return economyService.hasAccount(uuid);
            } catch (Exception e) {
                return economyService.hasAccount(parameters.getString(0));
            }
        }));

        serverRuntime.add("economyService", economyModule);
    }

}
