import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    private ViewInterface view;
    private List<User> model;
    private String tmp;

    public Controller() {
        view = new ConsoleView(this);
        model = new ArrayList<>();
        tmp = System.getProperty("java.io.tmpdir") + "for_converting.xml";
    }

    public void open(File source) throws IOException, SAXException, ParserConfigurationException {
        Utils.validate(source);
        model = Utils.parseSAX(source);
    }

    public void save(File dest) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(dest);
        StringBuilder builder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        builder.append("<users>\n");
        model.forEach((user -> builder.append(user.toTag())));
        builder.append("</users>\n");
        writer.print(builder.toString());
        writer.close();
    }

    public void toScreen() {
        view.show(model);
    }

    public void toHTML(String path) throws IOException, TransformerException {
        File temp = new File(tmp);
        save(temp);
        File page = new File(path);
        Utils.convert(temp, page, getClass().getClassLoader().getResource("html.xsl"));
        Desktop.getDesktop().open(page);
    }

    public void toText(String path) throws IOException, TransformerException {
        File temp = new File(tmp);
        save(temp);
        Utils.convert(temp, new File(path), getClass().getClassLoader().getResource("text.xsl"));
    }

    public boolean addUser(String surname, String name, String email, List<String> roles, List<String> phones) throws IncorrectParameterException {
        return model.add(new User(surname, name, email, roles, phones));
    }


    public User getUser(String surname, String name) throws IncorrectParameterException {
        int pos = Utils.indexOf(model, surname, name);
        if (pos == -1)
            throw new IncorrectParameterException("Пользователь не найден", -1);
        return model.get(pos);
    }

    public void editUser(String oldSurname, String oldName, String surname, String name, String email, List<String> roles, List<String> phones) throws IncorrectParameterException {
        int pos = Utils.indexOf(model, oldSurname, oldName);
        if (pos == -1)
            throw new IncorrectParameterException("Пользователь не найден", -1);
        User user = model.get(pos);
        user.setEmail(email);
        user.setRoles(roles);
        user.setPhones(phones);
        user.setSurname(surname);
        user.setName(name);
    }

    public void deleteUser(String surname, String name) throws IncorrectParameterException {
        int pos = Utils.indexOf(model, surname, name);
        if (pos == -1)
            throw new IncorrectParameterException("Пользователь не найден", -1);
        model.remove(pos);
    }
}
