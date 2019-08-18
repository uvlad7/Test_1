import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Utils {
    public static void convert(File source, File target, URL xsltURL) throws IOException, TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source styleSource = new StreamSource(xsltURL.openStream(), xsltURL.toExternalForm());
        Transformer transformer = factory.newTransformer(styleSource);
        transformer.transform(new StreamSource(source), new StreamResult(target));

    }

    public static void validate(File file) throws IOException, SAXException {
        SchemaFactory fact = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        Schema schema = fact.newSchema(Utils.class.getClassLoader().getResource("schema.xsd"));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(file));
    }

    public static List<User> parseSAX(File file) throws IOException, SAXException, ParserConfigurationException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        UserSAXHandler userHandler = new UserSAXHandler();
        parser.parse(file, userHandler);
        return userHandler.getUsers();
    }

    public static int indexOf(List<User> list, String surname, String name) {
        int size = list.size();
        User user;
        for (int i = 0; i < size; i++) {
            user = list.get(i);
            if (surname.equalsIgnoreCase(user.getSurname()) && name.equalsIgnoreCase(user.getName()))
                return i;
        }
        return -1;
    }
}
