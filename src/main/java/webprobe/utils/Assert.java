package webprobe.utils;

/**
 * Created by pleshivtsevsv on 27.06.2018.
 */
public class Assert {
    // Роняет тест и выводит сообщение об ошибке
    public static void pageAssert(String message){
        throw new RuntimeException(message);
    }

    // Сраврение двух строк на идентичность, возврат результата и сообщение в лог
    public static void pageAssertEquals (String actual, String expected, String message) {
        if (!actual.equals(expected)) {
            message = message + " Expected: " + expected + "; but Actual: " + actual;
            pageAssert(message);
        }
    }

    public static void pageAssertTrue (boolean condition_true, String message) {
        if (!condition_true)  pageAssert(message);
    }

    public static void shouldBeTrue (boolean condition_true, String message){
        if (!condition_true)  pageAssert(message);
    }


}
