import org.jetbrains.annotations.NotNull;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;

public class User implements Comparable<User> {

    public static final int INCORRECT_EMAIL = 0;
    public static final int INCORRECT_ROLES = 1;
    public static final int INCORRECT_PHONES = 2;

    private String name;
    private String surname;
    private String email;
    private List<String> roles;
    private List<String> phones;

    public User(String surname, String name, String email, List<String> roles, List<String> phones) throws IncorrectParameterException {
        setName(name);
        setSurname(surname);
        setEmail(email);
        setRoles(roles);
        setPhones(phones);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null)
            throw new InvalidParameterException("Имя не может быть null");
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        if (surname == null)
            throw new InvalidParameterException("Фамилия не может быть null");
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws IncorrectParameterException {
        if (!Verifier.emailVerify(email))
            throw new IncorrectParameterException("Неверный e-mail: " + email, INCORRECT_EMAIL);
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) throws IncorrectParameterException {
        if (roles.isEmpty() || roles.size() > 3)
            throw new IncorrectParameterException("Неверное число ролей", INCORRECT_ROLES);
        this.roles = roles;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) throws IncorrectParameterException {
        if (phones.isEmpty() || phones.size() > 3)
            throw new IncorrectParameterException("Неверное число телефонов", INCORRECT_PHONES);
        for (String phone : phones) {
            if (!Verifier.phoneVerify(phone))
                throw new IncorrectParameterException("Неверный телефон: " + phone, INCORRECT_PHONES);
        }
        this.phones = phones;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) &&
                surname.equals(user.surname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(surname).append(' ').append(name).append("\ne-mail: ");
        sb.append(email).append("\nРоли:\n");
        for (String role : roles)
            sb.append('\t').append(role).append('\n');
        sb.append("Телефоны:\n");
        for (String phone : phones)
            sb.append('\t').append(phone).append('\n');
        return sb.toString();
    }

    @Override
    public int compareTo(@NotNull User o) {
        int diff = surname.compareToIgnoreCase(o.surname);
        if (diff == 0)
            return name.compareToIgnoreCase(o.name);
        return diff;
    }

    public String toTag() {
        StringBuilder builder = new StringBuilder("\t<user surname=\"");
        builder.append(surname).append("\" name=\"").append(name).append("\">\n");
        builder.append("\t\t<email>").append(email).append("</email>\n");
        builder.append("\t\t<roles>\n");
        roles.forEach((role) -> builder.append("\t\t\t<role>").append(role).append("</role>\n"));
        builder.append("\t\t</roles>\n\t\t<phones>\n");
        phones.forEach((phone) -> builder.append("\t\t\t<phone>").append(phone).append("</phone>\n"));
        builder.append("\t\t</phones>\n\t</user>\n");
        return builder.toString();
    }
}
