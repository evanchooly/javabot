package javabot.javadoc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.sun.javadoc.Doclet;
import com.sun.javadoc.RootDoc;
import com.sun.tools.javadoc.Main;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StructureDoclet extends Doclet {
    private static final Log log = LogFactory.getLog(StructureDoclet.class);

    public static boolean start(RootDoc doc) {
        new StructureReference().process(doc);
        return false;
    }

    public void parse(File file) {
        File rootDir = null;
        try {
            ZipFile zip = new ZipFile(file);
            rootDir = new File(System.getProperty("java.io.tmpdir"), "javadoc" + System.currentTimeMillis());
            rootDir.mkdir();
            rootDir.deleteOnExit();
            Enumeration<? extends ZipEntry> enumeration = zip.entries();
            while(enumeration.hasMoreElements()) {
                extract(rootDir, zip, enumeration.nextElement());
            }
            String name = getClass().getSimpleName();
            String docletClass = getClass().getName();
            Main.execute(name, docletClass, new String[]{
                "-subpackages", "java",
                "-sourcepath", rootDir.getAbsolutePath()
            });
        } catch(IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        } finally {
            if(rootDir != null) {
                delete(rootDir);
            }
        }
    }

    private void delete(File file) {
        if(!file.isFile()) {
            for(File content : file.listFiles()) {
                delete(content);
            }
        }
        file.delete();
    }

    private void extract(File rootDir, ZipFile zip, ZipEntry entry) throws IOException {
        FileOutputStream outputStream = null;
        try {
            InputStream stream = zip.getInputStream(entry);
            File output = new File(rootDir, entry.getName());
            if(entry.isDirectory()) {
                output.mkdirs();
            } else {
                output.getParentFile().mkdirs();
                outputStream = new FileOutputStream(output);
                byte[] bytes = new byte[8 * 1024];
                int read;
                while((read = stream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
            }
        } finally {
            if(outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }
}
