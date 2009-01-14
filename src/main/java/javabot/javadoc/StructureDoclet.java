package javabot.javadoc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.sun.javadoc.Doclet;
import com.sun.javadoc.RootDoc;
import com.sun.tools.javadoc.Main;

/**
 * @noinspection StaticNonFinalField
 */
public class StructureDoclet extends Doclet {
    private static String apiName;
    private static String baseUrl;

    /**
     * @noinspection MethodOverridesStaticMethodOfSuperclass
     */
    public static boolean start(final RootDoc doc) {
        new StructureReference().process(doc, apiName, baseUrl);
        return false;
    }

    public static void parse(final File file, final String api, final String url, final String packages) {
        apiName = api;
        baseUrl = url;
        File rootDir = null;
        final boolean zipFile = file.getName().endsWith(".zip");
        try {
            System.out.println("processing " + file);
            rootDir = zipFile ? processZip(file) : file;
            final String name = StructureDoclet.class.getSimpleName();
            final String docletClass = StructureDoclet.class.getName();
            final List<String> args = new ArrayList<String>();
//            args.add("-cp build/main:build/test");
            args.add("-sourcepath");
            args.add(rootDir.getAbsolutePath());
            for (final String sub : packages.split(" ")) {
                args.add("-subpackages");
                args.add(sub);
            }
            System.out.println("Executing");
            System.out.println(args);
            System.out.println(System.getProperty("java.class.path"));
            for (final Entry<Object, Object> entry : System.getProperties().entrySet()) {
//                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
            Main.execute(name, docletClass, args.toArray(new String[args.size()]));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            if (zipFile && rootDir != null) {
                delete(rootDir);
            }
        }
    }

    private static File processZip(final File file) throws IOException {
        final ZipFile zip = new ZipFile(file);
        final File dir = new File(System.getProperty("java.io.tmpdir"), "javadoc" + System.currentTimeMillis());
        dir.mkdir();
        dir.deleteOnExit();
        final Enumeration<? extends ZipEntry> enumeration = zip.entries();
        while (enumeration.hasMoreElements()) {
            extract(dir, zip, enumeration.nextElement());
        }
        return dir;
    }

    private static void delete(final File file) {
        if (file.isDirectory()) {
            for (final File content : file.listFiles()) {
                delete(content);
            }
        }
        file.delete();
    }

    private static void extract(final File rootDir, final ZipFile zip, final ZipEntry entry) throws IOException {
        FileOutputStream outputStream = null;
        try {
            final InputStream stream = zip.getInputStream(entry);
            final File output = new File(rootDir, entry.getName());
            if (entry.isDirectory()) {
                output.mkdirs();
            } else {
                output.getParentFile().mkdirs();
                outputStream = new FileOutputStream(output);
                final byte[] bytes = new byte[8 * 1024];
                int read;
                while ((read = stream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            }
        } finally {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }

    public static void main(String[] args) {
        StructureDoclet.parse(new File(args[0]), args[1], args[2], args.length == 4 ? args[3] : "");
    }
}
