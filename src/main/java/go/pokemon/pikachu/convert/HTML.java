package go.pokemon.pikachu.convert;


import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTML {

    private String url;

    private Elements elements = new Elements();

    private HTML(String url, Elements elements) {
        this.url = url;
        this.elements = elements;
    }

    public HTML(String url, String html) {
        Document root = Parser.parse(html, url);
        elements = new Elements(root);
    }

    public HTML select(String rule) {
        Elements selectElements = elements.select(rule);
        return new HTML(url, selectElements);
    }

    public List<String> regex(String regex) {
        return regex(regex, 0);
    }

    public List<String> regex(String regex, int group) {
        List<String> stringList = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        // 现在创建 matcher 对象
        for (Element element : elements) {
            Matcher matcher = pattern.matcher(element.outerHtml());
            if (matcher.find()) {
                stringList.add(matcher.group(group));
            }
        }
        return stringList;
    }

    public List<String> attr(String key) {
        List<String> list = new ArrayList<>(elements.size());
        for (Element element : elements) {
            if ("href".equals(key) && !StringUtil.isBlank(element.baseUri())) {
                String attr = element.attr("abs:href");
                if (!attr.isBlank()) {
                    list.add(attr);
                }
            } else {
                list.add(element.attr(key));
            }
        }
        return list;
    }

    public Links links() {
        Links Links = new Links();
        Elements aElements = this.elements.select("a");
        for (Element element : aElements) {
            String attr;
            if (!StringUtil.isBlank(element.baseUri())) {
                attr = element.attr("abs:href");
            } else {
                attr = element.attr("href");
            }
            if (!attr.isBlank()) {
                Links.add(attr);
            }
        }
        return Links;
    }

    public List<String> all() {
        List<String> list = new ArrayList<>();
        for (Element element : elements) {
            String text = element.text();
            list.add(text);
        }
        return list;
    }

    public String text() {
        return elements.text();
    }

    @Override
    public String toString() {
        if (elements.size() == 1) {
            return elements.get(0).toString();
        }
        return elements.toString();
    }
}
