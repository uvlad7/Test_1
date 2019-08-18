import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class UserSAXHandler extends DefaultHandler {
    private String name;
    private String surname;
    private String email;
    private List<String> roles;
    private List<String> phones;
    private List<User> users;
    private boolean isRole;
    private boolean isPhone;
    private boolean isEmail;

    public UserSAXHandler() {
        users = new ArrayList<>();
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        super.startElement(namespaceURI, localName, qName, atts);
        switch (qName) {
            case "user":
                name = atts.getValue("name");
                surname = atts.getValue("surname");
                roles = new ArrayList<>();
                phones = new ArrayList<>();
                break;
            case "role":
                isRole = true;
                break;
            case "phone":
                isPhone = true;
                break;
            case "email":
                isEmail = true;
                break;
        }

    }

    @Override
    public void endElement(String s, String s1, String qName) throws SAXException {
        super.endElement(s, s1, qName);
        switch (qName) {
            case "role":
                isRole = false;
                break;
            case "phone":
                isPhone = false;
                break;
            case "email":
                isEmail = false;
                break;
            case "user":
                try {
                    users.add(new User(surname, name, email, roles, phones));
                } catch (IncorrectParameterException e) {
                    throw new SAXException(e.getMessage());
                }
                break;
        }
    }

    @Override
    public void characters(char[] chars, int i, int i1) throws SAXException {
        super.characters(chars, i, i1);
        if (isPhone) {
            phones.add(new String(chars, i, i1));
        }
        else if (isRole) {
            roles.add(new String(chars, i, i1));
        } else if (isEmail) {
            email = new String(chars, i, i1);
        }
    }

    public List<User> getUsers() {
        return users;
    }
}
