import javax.swing.text.NumberFormatter;
import java.text.ParseException;

public class MyNumberFormatter extends NumberFormatter {

    @Override
    public Object stringToValue(String text) throws ParseException{
        super.stringToValue(text);
        if (!text.isEmpty()){

        }
        return null;
    }
}
