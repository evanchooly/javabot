package javabot.admin;

import java.io.File;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import javabot.javadoc.StructureDoclet;

/**
 * Created Jul 19, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
public class JavadocPanel extends Panel {
    public JavadocPanel(String id) {
        super(id);

        FileUploadForm ajaxSimpleUploadForm = new FileUploadForm("form");
        ajaxSimpleUploadForm.add(new UploadProgressBar("progress", ajaxSimpleUploadForm));
        add(ajaxSimpleUploadForm);
    }

    private class FileUploadForm extends Form {
        private FileUploadField fileUploadField;

        public FileUploadForm(String name) {
            super(name);
            setMultiPart(true);
            add(fileUploadField = new FileUploadField("fileInput"));
        }

        @Override
        protected void onSubmit() {
            FileUpload upload = fileUploadField.getFileUpload();
            if(upload != null) {
                try {
                    File newFile = File.createTempFile("source", "zip");
                    newFile.createNewFile();
                    upload.writeTo(newFile);
                    info("saved file: " + upload.getClientFileName());
                    new StructureDoclet().parse(newFile);
                }
                catch(Exception e) {
                    throw new IllegalStateException("Unable to write file");
                }
            }
        }
    }


}
