package io.github.djxy.spongejs.module.modules;

import com.eclipsesource.v8.*;
import io.github.djxy.spongejs.converters.Converter;
import io.github.djxy.spongejs.module.Module;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;

import java.util.Map;
import java.util.Optional;

/**
 * Created by samuelmarchildon-lavoie on 16-09-14.
 */
public class PermissionModule implements Module {

    @Override
    public void initialize(V8 serverRuntime) {
        V8Object permissionModule = new V8Object(serverRuntime);
        Optional<PermissionService> service = Sponge.getServiceManager().provide(PermissionService.class);

        if(!service.isPresent())
            return;

        PermissionService permissionService = service.get();

        permissionModule.add("getDefaults", new V8Function(serverRuntime, (receiver, parameters) -> Converter.convertToV8(serverRuntime, Subject.class, permissionService.getDefaults())));
        permissionModule.add("getGroupSubjects", new V8Function(serverRuntime, (receiver, parameters) -> Converter.convertToV8(serverRuntime, SubjectCollection.class, permissionService.getGroupSubjects())));
        permissionModule.add("getUserSubjects", new V8Function(serverRuntime, (receiver, parameters) -> Converter.convertToV8(serverRuntime, SubjectCollection.class, permissionService.getUserSubjects())));
        permissionModule.add("getSubjects", new V8Function(serverRuntime, (receiver, parameters) -> Converter.convertToV8(serverRuntime, SubjectCollection.class, permissionService.getSubjects(parameters.getString(0)))));
        permissionModule.add("getKnownSubjects", new V8Function(serverRuntime, (receiver, parameters) -> {
            V8Object collections = new V8Object(serverRuntime);
            Map<String,SubjectCollection> map = permissionService.getKnownSubjects();

            for(String key : map.keySet())
                collections.add(key, (V8Value) Converter.convertToV8(serverRuntime, SubjectCollection.class, map.get(key)));

            return collections;
        }));

        serverRuntime.add("permissionService", permissionModule);
    }

}
