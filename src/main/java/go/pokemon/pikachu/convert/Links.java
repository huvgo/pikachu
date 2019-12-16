package go.pokemon.pikachu.convert;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Links extends ArrayList<String> {


    public Links regex(String regex) {
        return regex(regex, 0);
    }

    public Links regex(String regex, int group) {
        Links links = new Links();
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        // 现在创建 matcher 对象
        for (String textNode : this) {
            Matcher matcher = pattern.matcher(textNode);
            if (matcher.find()) {
                links.add(matcher.group(group));
            }
        }
        return links;
    }
}
