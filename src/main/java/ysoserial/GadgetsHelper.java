package ysoserial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import ysoserial.payloads.BeanShell1;
import ysoserial.payloads.C3P0;
import ysoserial.payloads.CommonsBeanutils1;
import ysoserial.payloads.CommonsCollections10;
import ysoserial.payloads.CommonsCollections11;
import ysoserial.payloads.CommonsCollections3ForLoadJar;
import ysoserial.payloads.CommonsCollections5ForLoadJar;
import ysoserial.payloads.CommonsCollections6ForLoadJar;
import ysoserial.payloads.CommonsCollections8;
import ysoserial.payloads.CommonsCollections9;
import ysoserial.payloads.Groovy1;
import ysoserial.payloads.Jdk7u21;
import ysoserial.payloads.Myfaces2;
import ysoserial.payloads.ObjectPayload;
import ysoserial.payloads.ROME;
import ysoserial.payloads.Spring1;
import ysoserial.payloads.Spring2;
import ysoserial.payloads.Vaadin1;

/**
 * @author threedr3am
 */
public class GadgetsHelper {

    private static final Map<String, List<Class<? extends ObjectPayload>>> gadgets = new HashMap<>();

    static {
        List<Class<? extends ObjectPayload>> cmds = new ArrayList<>();
        cmds.add(CommonsBeanutils1.class);
        cmds.add(CommonsCollections8.class);
        cmds.add(CommonsCollections9.class);
        cmds.add(CommonsCollections10.class);
        cmds.add(CommonsCollections11.class);
        cmds.add(Jdk7u21.class);
        cmds.add(Vaadin1.class);
        cmds.add(ROME.class);
        cmds.add(Spring1.class);
        cmds.add(Spring2.class);
        cmds.add(BeanShell1.class);
        cmds.add(Groovy1.class);
        gadgets.put("CMD", cmds);

        List<Class<? extends ObjectPayload>> jar = new ArrayList<>();
        jar.add(CommonsCollections3ForLoadJar.class);
        jar.add(CommonsCollections5ForLoadJar.class);
        jar.add(CommonsCollections6ForLoadJar.class);
        gadgets.put("JAR", jar);

        List<Class<? extends ObjectPayload>> codebase = new ArrayList<>();
        codebase.add(C3P0.class);
        codebase.add(Myfaces2.class);
        gadgets.put("CODEBASE", codebase);
    }

    public static String getName(String type, int index) {
        Optional<String> name = Optional.ofNullable(gadgets.get(type)).map(gs -> gs.get(index)).map(c -> c.getName());
        if (name.isPresent()) {
            return name.get();
        }
        return "";
    }

    public static List<Class<? extends ObjectPayload>> get(String type) {
        return gadgets.get(type);
    }
}
