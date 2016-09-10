package io.github.djxy.spongejs.module;

import com.eclipsesource.v8.V8;

/**
 * Created by Samuel on 2016-09-07.
 */
public interface Module {

    public void initialize(V8 serverRuntime);

}
