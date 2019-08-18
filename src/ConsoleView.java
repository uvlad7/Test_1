import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConsoleView implements ViewInterface {
    private static String[] menu = {"1 - файл, 2 - экспорт, 3 - пользователи, 0 - выход",
            "1 - открыть xml файл, 2 - сохранить в xml файл, 0 - главное меню",
            "1 - вывести на экран, 2 - сохранить в html, 3 - сохранить как текст, 0 - главное меню",
            "1 - добавить пользователя, 2 - редактировать пользователя, 3 - удалить пользователя, 0 - главное меню"};
    private BufferedReader reader;
    private Controller controller;
    private int context;
    private boolean run;
    private Thread commandsThread;

    public ConsoleView(Controller controller) {
        this.controller = controller;
        reader = new BufferedReader(new InputStreamReader(System.in));
        context = 0;
        run = true;
        commandsThread = new Thread(this::dispatch);
        commandsThread.start();
    }

    private void dispatch() {
        while (run) {
            System.out.println(menu[context]);
            try {
                process(Integer.parseInt(reader.readLine()));
            } catch (NumberFormatException | IOException e) {
                System.err.println("Неверная команда");
            }

        }
    }

    private void process(int command) {
        switch (context) {
            case 0: {
                mainMenu(command);
                break;
            }
            case 1: {
                fileMenu(command);
                break;
            }
            case 2: {
                exportMenu(command);
                break;
            }
            case 3: {
                userMenu(command);
                break;
            }
        }
    }

    private void mainMenu(int command) {
        if (command < 0 || command > 3)
            throw new NumberFormatException();
        if (command == 0)
            run = false;
        else context = command;
    }

    private void fileMenu(int command) {
        switch (command) {
            case 0: {
                context = 0;
                break;
            }
            case 1: {
                open();
                break;
            }
            case 2: {
                save();
                break;
            }
            default: {
                throw new NumberFormatException();
            }
        }
    }

    private void open() {
        try {
            controller.open(new File(getPath()));
        } catch (IOException e) {
            System.err.println("Файл не найден");
        } catch (SAXException e) {
            System.err.println("Некорректный файл");
        } catch (ParserConfigurationException e) {
            System.err.println("Попробуйте ещё раз");
        }
    }

    private void save() {
        try {
            controller.save(new File(getPath()));
        } catch (FileNotFoundException e) {
            System.err.println("Файл не найден");
        } catch (IOException e) {
            System.err.println("Попробуйте ещё раз");
        }
    }

    private void exportMenu(int command) {
        switch (command) {
            case 0: {
                context = 0;
                break;
            }
            case 1: {
                controller.toScreen();
                break;
            }
            case 2: {
                try {
                    controller.toHTML(getPath());
                } catch (IOException | TransformerException e) {
                    System.err.println("Попробуйте ещё раз");
                }
                break;
            }
            case 3: {
                try {
                    controller.toText(getPath());
                } catch (IOException | TransformerException e) {
                    System.err.println("Попробуйте ещё раз");
                }
                break;
            }
            default: {
                throw new NumberFormatException();
            }
        }
    }

    private void userMenu(int command) {
        switch (command) {
            case 0: {
                context = 0;
                break;
            }
            case 1: {
                try {
                    add();
                } catch (IOException e) {
                    System.err.println("Попробуйте ещё раз");
                }
                break;
            }
            case 2: {
                try {
                    edit();
                } catch (IOException e) {
                    System.err.println("Попробуйте ещё раз");
                }
                break;
            }
            case 3: {
                try {
                    delete();
                } catch (IOException e) {
                    System.err.println("Попробуйте ещё раз");
                }
                break;
            }
            default: {
                throw new NumberFormatException();
            }
        }
    }

    private void add() throws IOException {
        System.out.println("Введите фамилию");
        String surname = reader.readLine();
        System.out.println("Введите имя");
        String name = reader.readLine();
        System.out.println("Введите e-mail");
        String email = reader.readLine();
        System.out.println("Введите список ролей");
        List<String> roles = Arrays.asList(reader.readLine().split("[,;]+"));
        System.out.println("Введите список телефонов");
        List<String> phones = Arrays.asList(reader.readLine().split("[,;]+"));
        try {
            controller.addUser(surname, name, email, roles, phones);
        } catch (IncorrectParameterException e) {
            addAgain(surname, name, email, roles, phones, e);
        }
    }

    private void addAgain(String surname, String name, String email, List<String> roles, List<String> phones, IncorrectParameterException e) throws IOException {
        System.err.println(e.getMessage());
        System.out.println("Изменить? Да/отмена");
        if (reader.readLine().equalsIgnoreCase("да")) {
            try {
                switch (e.getCode()) {
                    case User.INCORRECT_EMAIL: {
                        System.out.println("Введите e-mail");
                        email = reader.readLine();
                        break;
                    }
                    case User.INCORRECT_ROLES: {
                        System.out.println("Введите список ролей");
                        roles = Arrays.asList(reader.readLine().split("[,;]+"));
                        break;
                    }
                    case User.INCORRECT_PHONES: {
                        System.out.println("Введите список телефонов");
                        phones = Arrays.asList(reader.readLine().split("[,;]+"));
                        break;
                    }
                }
                controller.addUser(surname, name, email, roles, phones);
            } catch (IncorrectParameterException ex) {
                addAgain(surname, name, email, roles, phones, ex);
            }
        }
    }

    private void edit() throws IOException {
        try {
            System.out.println("Введите фамилию");
            String oldSurname = reader.readLine();
            System.out.println("Введите имя");
            String oldName = reader.readLine();
            User user = controller.getUser(oldSurname, oldName);
            System.out.println(user);
            System.out.println("Перечислите поля, которые желаете изменить");
            System.out.println("1 - фамилия, 2 - имя, 3 - e-mail, 4 - роли, 5 - телефоны");
            List<Integer> options = Arrays.stream(reader.readLine().split("[,;]+")).map(Integer::parseInt).collect(Collectors.toList());
            String surname = user.getSurname();
            String name = user.getName();
            String email = user.getEmail();
            List<String> roles = user.getRoles();
            List<String> phones = user.getPhones();
            for (Integer option : options) {
                switch (option) {
                    case 1: {
                        System.out.println("Введите новую фамилию");
                        surname = reader.readLine();
                        break;
                    }
                    case 2: {
                        System.out.println("Введите новое имя");
                        name = reader.readLine();
                        break;
                    }
                    case 3: {
                        System.out.println("Введите новый e-mail");
                        email = reader.readLine();
                        break;
                    }
                    case 4: {
                        System.out.println("Введите новый список ролей");
                        roles = Arrays.asList(reader.readLine().split("[,;]+"));
                        break;
                    }
                    case 5: {
                        System.out.println("Введите новый список телефонов");
                        phones = Arrays.asList(reader.readLine().split("[,;]+"));
                        break;
                    }
                    default: {
                        throw new NumberFormatException();
                    }

                }
            } try {
                controller.editUser(oldSurname, oldName, surname, name, email, roles, phones);
            } catch (IncorrectParameterException e) {
                editAgain(oldSurname, oldName, surname, name, email, roles, phones, e);
            }
        } catch (IncorrectParameterException e) {
            System.err.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Неверная команда");
        }

    }

    private void editAgain(String oldSurname, String oldName, String surname, String name, String email, List<String> roles, List<String> phones, IncorrectParameterException e) throws IOException {
        System.err.println(e.getMessage());
        System.out.println("Изменить? Да/отмена");
        if (reader.readLine().equalsIgnoreCase("да")) {
            try {
                switch (e.getCode()) {
                    case User.INCORRECT_EMAIL: {
                        System.out.println("Введите новый e-mail");
                        email = reader.readLine();
                        break;
                    }
                    case User.INCORRECT_ROLES: {
                        System.out.println("Введите новый список ролей");
                        roles = Arrays.asList(reader.readLine().split("[,;]+"));
                        break;
                    }
                    case User.INCORRECT_PHONES: {
                        System.out.println("Введите новый список телефонов");
                        phones = Arrays.asList(reader.readLine().split("[,;]+"));
                        break;
                    }
                }
                controller.editUser(oldSurname, oldName, surname, name, email, roles, phones);
            } catch (IncorrectParameterException ex) {
                editAgain(oldSurname, oldName, surname, name, email, roles, phones, ex);
            }
        }
    }

    private void delete() throws IOException {
        System.out.println("Введите фамилию");
        String surname = reader.readLine();
        System.out.println("Введите имя");
        String name = reader.readLine();
        try {
            controller.deleteUser(surname, name);
        } catch (IncorrectParameterException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void show(List<User> users) {
        users.forEach(System.out::println);
    }

    private String getPath() throws IOException {
        System.out.println("Введите имя файла");
        return reader.readLine();
    }
}
