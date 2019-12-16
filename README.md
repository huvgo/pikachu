# pikachu

去吧，皮卡丘！简单方便的网络爬虫

## 快速开始

``` java

public class PiKaChuExample {
    /**
     * 测试京东 手机分类抓取详情页
     * https://list.jd.com/list.html?cat=9987,653,655
     */       
    public static void main(String[] args) {
        // config可以做相关配置，seedRequest是爬虫的种子请求
        Pikachu.builder().configurer(config -> config.setThreadNum(3).setTimeOut(3000)).seedRequest("https://list.jd.com/list.html?cat=9987,653,655")
                .responseHandler(context -> {
                    // 处理相关逻辑
                    HTML html = context.response().getHtml();
                    Links detail = html.links().regex("(https://item.jd.com/[0-9][0-9]*.html)");
                    System.out.println("手机详情链接 = " + detail);
                    Links links = html.select("#J_bottomPage > span.p-num > a").links();
                    // 添加新的请求任务
                    context.addRequest(links.toArray(new String[0]));
                    System.out.println("列表页 = " + links);
                }).build().go();
    }
  
}
```

## 结合spring的例子
``` java
@Component
public class PiKaChuExample {
    /**
     * 测试京东 手机分类抓取详情页
     * https://list.jd.com/list.html?cat=9987,653,655
     */       
    @Bean
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
}
```