package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import org.spongepowered.api.service.economy.account.UniqueAccount;

/**
 * Created by Samuel on 2016-09-07.
 */
public class UniqueAccountConverter extends AccountConverter<UniqueAccount> {

    @Override
    protected V8Object setV8Object(V8Object v8Object, V8 v8, UniqueAccount account) {
        v8Object.add("getUniqueID", new V8Function(v8, (receiver, parameters) -> new UUIDConverter().convertToV8(v8, account.getUniqueId())));

        return super.setV8Object(v8Object, v8, account);
    }

}
