package io.github.djxy.spongejs.converters;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8Value;
import com.google.common.reflect.ClassPath;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Samuel on 2016-09-07.
 */
public abstract class Converter<V, T> {

    private static HashMap<Class, Converter> converters = new HashMap<>();
    private static HashMap<Class, V8ObjectCreator> objectCreators = new HashMap<>();

    public static <Y extends Object> Y convertFromV8(Class<Y> type, Object o){
        if(!converters.containsKey(type) || o == null)
            return null;

        return (Y) converters.get(type).convertFromV8(o);
    }

    public static <Y extends Object> Object convertToV8(V8 v8, Class<Y> type, Y o){
        if(!objectCreators.containsKey(type) || o == null)
            return null;

        return objectCreators.get(type).createV8Object(v8, o);
    }

    public static <Y extends Object> Object editV8Object(V8Object object, Class<Y> type, Y o){
        if(!objectCreators.containsKey(type))
            return null;

        return ((V8ObjectCreatorV8Object)objectCreators.get(type)).editV8Object(object, o);
    }

    public static <Y extends Object> Object convertSetToV8(V8 v8, Class<Y> type, Set<Y> o){
        if(!objectCreators.containsKey(type))
            return null;

        V8Array array = new V8Array(v8);

        for(Y y : o){
            Object object = objectCreators.get(type).createV8Object(v8, y);

            if(object instanceof V8Value)
                array.push((V8Value) object);
            else if(object instanceof String)
                array.push((String) object);
            else if(object instanceof Integer)
                array.push((Integer) object);
            else if(object instanceof Double)
                array.push((Double) object);
            else if(object instanceof Boolean)
                array.push((Boolean) object);
        }

        return array;
    }

    public static <Y extends Object> Object convertListToV8(V8 v8, Class<Y> type, List<Y> o){
        if(!objectCreators.containsKey(type))
            return null;

        V8Array array = new V8Array(v8);

        for(Y y : o){
            Object object = objectCreators.get(type).createV8Object(v8, y);

            if(object instanceof V8Value)
                array.push((V8Value) object);
            else if(object instanceof String)
                array.push((String) object);
            else if(object instanceof Integer)
                array.push((Integer) object);
            else if(object instanceof Double)
                array.push((Double) object);
            else if(object instanceof Boolean)
                array.push((Boolean) object);
        }

        return array;
    }

    public static <Y extends Object> List<Y> convertListFromV8(Class<Y> type, Object o){
        List<Y> list = new ArrayList<>();
        V8Array v8Array = (V8Array) o;

        for(int i = 0; i < v8Array.length(); i++)
            list.add(convertFromV8(type, v8Array.get(i)));

        return list;
    }

    public static <Y extends Object> Set<Y> convertSetFromV8(Class<Y> type, Object o){
        Set<Y> set = new HashSet<>();
        V8Array v8Array = (V8Array) o;

        for(int i = 0; i < v8Array.length(); i++)
            set.add(convertFromV8(type, v8Array.get(i)));

        return set;
    }

    abstract public T convertToV8(V8 v8, V v);

    public V convertFromV8(Object o){
        return null;
    }

    private interface V8ObjectCreator {

        public <Y extends Object> Object createV8Object(V8 v8, Y o);

    }

    private static class V8ObjectCreatorPrimitve implements V8ObjectCreator {

        private final Converter converter;

        public V8ObjectCreatorPrimitve(Converter converter) {
            this.converter = converter;
        }

        @Override
        public String toString() {
            return "V8ObjectCreatorPrimitve{" +
                    "converter=" + converter +
                    '}';
        }

        @Override
        public <Y> Object createV8Object(V8 v8, Y o) {
            return converter.convertToV8(v8, o);
        }
    }

    private static class V8ObjectCreatorV8Object implements V8ObjectCreator {

        private final Set<ConverterV8Object> converters;

        public V8ObjectCreatorV8Object() {
            this.converters = new HashSet<>();
        }

        @Override
        public String toString() {
            return "V8ObjectCreatorV8Object{" +
                    "converters=" + converters +
                    '}';
        }

        @Override
        public <Y> Object createV8Object(V8 v8, Y o) {
            V8Object v8Object = new V8Object(v8);

            for(ConverterV8Object converterV8Object : converters)
                converterV8Object.setV8Object(v8Object, v8, o);

            return v8Object;
        }

        public <Y extends Object> Object editV8Object(V8Object v8Object, Y o){
            for(ConverterV8Object converterV8Object : converters)
                converterV8Object.setV8Object(v8Object, v8Object.getRuntime(), o);

            return v8Object;
        }

    }

    public static void init(){
        if(!converters.isEmpty())
            return;

        try {
            for (ClassPath.ClassInfo info : ClassPath.from(Converter.class.getClassLoader()).getTopLevelClasses("io.github.djxy.spongejs.converters")) {
                Class clazz = info.load();

                Annotation annotation = clazz.getAnnotation(ConverterInfo.class);

                if(annotation != null) {
                    converters.put(((ConverterInfo) annotation).type(), (Converter) clazz.getConstructor().newInstance());

                    if(((ConverterInfo) annotation).isV8Primitive())
                        objectCreators.put(((ConverterInfo) annotation).type(), new V8ObjectCreatorPrimitve(converters.get(((ConverterInfo) annotation).type())));
                    else
                        objectCreators.put(((ConverterInfo) annotation).type(), new V8ObjectCreatorV8Object());
                }
            }

            initObjectCreators();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void initObjectCreators(){
        for(Class c : converters.keySet()) {
            if (objectCreators.get(c) instanceof V8ObjectCreatorV8Object) {
                initObjectCreator(c, (V8ObjectCreatorV8Object) objectCreators.get(c));
                ((V8ObjectCreatorV8Object) objectCreators.get(c)).converters.add((ConverterV8Object) converters.get(c));
            }
        }
    }

    private static void initObjectCreator(Class scan, V8ObjectCreatorV8Object objectCreatorV8Object){
        for(Class c : scan.getInterfaces()){
            if(converters.containsKey(c))
                objectCreatorV8Object.converters.add((ConverterV8Object) converters.get(c));

            initObjectCreator(c, objectCreatorV8Object);
        }
    }

}
