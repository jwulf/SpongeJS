package io.github.djxy.spongejs.converter;

import com.eclipsesource.v8.*;
import com.google.common.reflect.ClassPath;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2016-09-07.
 */
public abstract class Converter<V, T> {

    private static HashMap<Class, Converter> converters = new HashMap<>();
    private static HashMap<Class, V8ObjectCreator> objectCreators = new HashMap<>();
    private static ConcurrentHashMap<Long, CopyOnWriteArrayList<V8Value>> v8Values = new ConcurrentHashMap<>();

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

    public static V8Function registerV8Function(V8Function value, Long uniqueIdentifier){
        registerV8Value(value, uniqueIdentifier);
        return value;
    }

    public static V8Array registerV8Array(V8Array value, Long uniqueIdentifier){
        registerV8Value(value, uniqueIdentifier);
        return value;
    }

    public static V8Object registerV8Object(V8Object value, Long uniqueIdentifier){
        registerV8Value(value, uniqueIdentifier);
        return value;
    }

    public synchronized static V8Value registerV8Value(V8Value value, Long uniqueIdentifier){
        if(!v8Values.containsKey(uniqueIdentifier))
            v8Values.put(uniqueIdentifier, new CopyOnWriteArrayList<>());

        v8Values.get(uniqueIdentifier).add(value);
        return value;
    }

    public static void releaseRegistredValues(Long uniqueIdentifier){
        if(v8Values.containsKey(uniqueIdentifier)) {
            for (V8Value value : v8Values.get(uniqueIdentifier))
                value.release();

            v8Values.get(uniqueIdentifier).clear();
            v8Values.remove(uniqueIdentifier);
        }
    }

    public static <Y extends Object> Object convertIterableToV8(V8 v8, Class<Y> type, Iterable<Y> o){
        if(!objectCreators.containsKey(type))
            return null;

        V8Array array = new V8Array(v8);

        for(Y y : o){
            Object object = objectCreators.get(type).createV8Object(v8, y);

            if(object instanceof V8Value)
                array.push(registerV8Value((V8Value) object, array.getHandle()));
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
                converterV8Object.setV8Object(v8Object, v8, o, v8Object.getHandle());

            return v8Object;
        }

        public <Y extends Object> Object editV8Object(V8Object v8Object, Y o){
            for(ConverterV8Object converterV8Object : converters)
                converterV8Object.setV8Object(v8Object, v8Object.getRuntime(), o, v8Object.getHandle());

            return v8Object;
        }

    }

    public synchronized static void init(){
        if(!converters.isEmpty())
            return;

        try {
            for (ClassPath.ClassInfo info : ClassPath.from(Converter.class.getClassLoader()).getTopLevelClasses("io.github.djxy.spongejs.converter.converters")) {
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
                initObjectCreatorInterfaces(c, (V8ObjectCreatorV8Object) objectCreators.get(c));
                initObjectCreatorSuperClass(c, (V8ObjectCreatorV8Object) objectCreators.get(c));
                ((V8ObjectCreatorV8Object) objectCreators.get(c)).converters.add((ConverterV8Object) converters.get(c));
                ((V8ObjectCreatorV8Object) objectCreators.get(c)).converters.add((ConverterV8Object) converters.get(Object.class));
            }
        }
    }

    private static void initObjectCreatorInterfaces(Class scan, V8ObjectCreatorV8Object objectCreatorV8Object){
        for(Class c : scan.getInterfaces()){
            if(converters.containsKey(c))
                objectCreatorV8Object.converters.add((ConverterV8Object) converters.get(c));

            initObjectCreatorInterfaces(c, objectCreatorV8Object);
        }
    }

    private static void initObjectCreatorSuperClass(Class scan, V8ObjectCreatorV8Object objectCreatorV8Object){
        if(scan.getSuperclass() != null) {
            if(converters.containsKey(scan.getSuperclass()))
                objectCreatorV8Object.converters.add((ConverterV8Object) converters.get(scan.getSuperclass()));

            initObjectCreatorSuperClass(scan.getSuperclass(), objectCreatorV8Object);
        }
    }

}
