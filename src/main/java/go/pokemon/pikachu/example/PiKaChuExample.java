package go.pokemon.pikachu.example;


import go.pokemon.pikachu.Pikachu;
import go.pokemon.pikachu.Response;
import go.pokemon.pikachu.convert.HTML;
import go.pokemon.pikachu.convert.JSON;
import go.pokemon.pikachu.convert.Links;

import java.util.List;

public class PiKaChuExample {
    public static void main(String[] args) {
        Pikachu.builder().configurer(config -> config.setThreadNum(3).setTimeOut(3000)).seedRequest("https://list.jd.com/list.html?cat=9987,653,655")
                .responseHandler(context -> {
                    HTML html = context.response().getHtml();
                    Links detail = html.links().regex("(https://item.jd.com/[0-9][0-9]*.html)");
                    System.out.println("手机详情链接 = " + detail);
                    Links links = html.select("#J_bottomPage > span.p-num > a").links();
                    // 添加新的请求任务
                    context.addRequest(links.toArray(new String[0]));
                    System.out.println("列表页 = " + links);
                }).build().go();
    }


    /**
     * 测试京东 手机分类抓取详情页
     * https://list.jd.com/list.html?cat=9987,653,655
     */
    public Pikachu JDCrawler() {
        return Pikachu.builder().configurer(config -> config.setThreadNum(3).setTimeOut(3000)).seedRequest("https://list.jd.com/list.html?cat=9987,653,655")
                .responseHandler(context -> {
                    HTML html = context.response().getHtml();
                    Links detail = html.links().regex("(https://item.jd.com/[0-9][0-9]*.html)");
                    System.out.println("手机详情链接 = " + detail);
                    Links links = html.select("#J_bottomPage > span.p-num > a").links();
                    // 添加新的请求任务
                    context.addRequest(links.toArray(new String[0]));
                    System.out.println("列表页 = " + links);
                }).build();
    }

    /**
     * 百度百科java
     * https://baike.baidu.com/item/java
     */
    public Pikachu BaiKeCrawler() {
        return Pikachu.builder().seedRequest("https://baike.baidu.com/item/java")
                .responseHandler(context -> {
                    HTML html = context.response().getHtml();
                    HTML contentHtml = html.select("body > div.body-wrapper > div.content-wrapper > div > div.main-content > div.lemmaWgt-lemmaCatalog > div > div > ol:nth-child(1) > li:nth-child(1) > span.text > a");
                    String text = contentHtml.text();
                    System.out.println("text = " + text);
                }).build();
    }

    /**
     * 测试爬取wufazhuce网站、
     * 获取每日图文第一个图片
     * http://m.wufazhuce.com/one
     */
    public Pikachu OneCrawler() {
        return Pikachu.builder().seedRequest("http://m.wufazhuce.com/one")
                .responseHandler(context -> {
                    Response response = context.response();
                    List<String> tokens = response.getHtml().regex("One.token = '(.*?)'", 1);
                    if (tokens != null && tokens.size() > 0) {
                        String token = tokens.get(0);
                        System.out.println("token = " + token);
                        context.addRequest("http://m.wufazhuce.com/one/ajaxlist/0?_token=" + token);
                    } else {
                        JSON json = response.getJson();
                        String imgUrl = json.jsonPath("$.data[0].img_url");
                        System.out.println("imgUrl = " + imgUrl);
                    }
                }).build();
    }
}
