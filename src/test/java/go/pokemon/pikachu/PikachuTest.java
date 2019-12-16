package go.pokemon.pikachu;


import go.pokemon.pikachu.convert.HTML;
import go.pokemon.pikachu.convert.JSON;
import go.pokemon.pikachu.convert.Links;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

@Slf4j
public class PikachuTest {
    /**
     * 百度百科java
     * https://baike.baidu.com/item/java
     */
    @Test
    public void testBaiKe() {
        Pikachu.builder().configurer(config -> config.setThreadNum(20).setTimeOut(3000)).seedRequest("https://baike.baidu.com/item/java")
                .responseHandler(context -> {
                    HTML html = context.response().getHtml();
                    HTML selector = html.select("body > div.body-wrapper > div.content-wrapper > div > div.main-content > div.lemmaWgt-lemmaCatalog > div > div > ol:nth-child(1) > li:nth-child(1) > span.text > a");
                    Assert.assertEquals(selector.text(), "发展历程");
                }).build().go();
    }

    /**
     * 测试京东 手机分类抓取详情页
     * https://list.jd.com/list.html?cat=9987,653,655
     */
    @Test
    public void testJd() {
        Pikachu.builder().configurer(config -> config.setThreadNum(3).setTimeOut(3000)).seedRequest("https://list.jd.com/list.html?cat=9987,653,655")
                .responseHandler(context -> {
                    HTML html = context.response().getHtml();
                    Links detail = html.links().regex("(https://item.jd.com/[0-9][0-9]*.html)");
                    log.info("手机详情链接 = " + detail);
                    Links links = html.select("#J_bottomPage > span.p-num > a").links();
                    context.addRequest(links.toArray(new String[0]));
                    log.info("列表页 = " + links);
                }).build().go();
    }

    /**
     * 测试爬取wufazhuce网站、
     * 获取每日图文第一个图片
     * http://m.wufazhuce.com/one
     */
    @Test
    public void testOne() {
        Pikachu.builder().seedRequest("http://m.wufazhuce.com/one")
                .responseHandler(context -> {
                    Response response = context.response();
                    List<String> tokens = response.getHtml().regex("One.token = '(.*?)'", 1);
                    if (tokens != null && tokens.size() > 0) {
                        String token = tokens.get(0);
                        log.info("token = " + token);
                        context.addRequest("http://m.wufazhuce.com/one/ajaxlist/0?_token=" + token);
                    } else {
                        JSON json = response.getJson();
                        String imgUrl = json.jsonPath("$.data[0].img_url");
                        log.info("imgUrl = " + imgUrl);
                    }
                }).build().go();
    }

}