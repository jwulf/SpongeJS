package io.github.djxy.spongejs.converter.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Object;
import io.github.djxy.spongejs.converter.ConverterInfo;
import io.github.djxy.spongejs.converter.ConverterV8Object;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by Samuel on 2016-09-07.
 */
@ConverterInfo(type = UniqueAccount.class)
public class UniqueAccountConverter extends ConverterV8Object<UniqueAccount> {

    @Override
    public UniqueAccount convertFromV8(Object o) {
        EconomyService service = Sponge.getServiceManager().provide(EconomyService.class).get();
        String identifier = null;

        if(o instanceof String)
            identifier = (String) o;
        else if(o instanceof V8Object){
            if(((V8Object) o).contains("getUniqueID"))
                identifier = ((V8Object) o).executeStringFunction("getUniqueID", null);
        }

        if(identifier == null)
            throw new IllegalArgumentException("Should be a string or an account.");

        try{
            UUID uuid = UUID.fromString(identifier);
            Optional<UniqueAccount> uniqueAccountOpt = service.getOrCreateAccount(uuid);

            if(uniqueAccountOpt.isPresent())
                return uniqueAccountOpt.get();
        } catch (Exception e){}

        return super.convertFromV8(o);
    }

    @Override
    public void setV8Object(V8Object v8Object, V8 v8, UniqueAccount account, UUID uniqueIdentifier) {}

}
