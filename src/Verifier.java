import java.util.regex.Pattern;

public class Verifier {
    //Может сойдёт и посложнее проверка?
    private static Pattern emailPattern = Pattern.compile("\\A[-!#$%&'*+/=?^_`{|}~a-z0-9]+(?:\\.[-!#$%&'*+/=?^_`{|}~a-z0-9]+)*@(?:[a-z0-9](?:[-a-z0-9]{0,61}[a-z0-9])?\\.)+" +
            "(?:com|org|net|int|edu|gov|mil|arpa|[a-z][a-z])\\z", Pattern.CASE_INSENSITIVE);

    //Раз уж телефоны мобильные и белорусские, почему бы не перечислить все коды операторов?
    private static Pattern mobilePhonePattern = Pattern.compile("\\A375(?:25|29|33|44) \\d{7}\\z");

    public static boolean emailVerify(String text) {
        return emailPattern.matcher(text).matches();
    }

    public static boolean phoneVerify(String text) {
        return mobilePhonePattern.matcher(text).matches();
    }
}
