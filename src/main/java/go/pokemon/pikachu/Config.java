package go.pokemon.pikachu;


import java.util.HashMap;
import java.util.Map;

public class Config {

    int threadNum = 3;

    String userAgent;

    // 线程睡眠时间
    int sleepTime = 3000;

    // 重试次数
    int retryTimes = 3;

    // 单位毫秒
    int timeOut = 5000;

    Map<String, String> headers = new HashMap<>();


    public Config setThreadNum(int threadNum) {
        this.threadNum = threadNum;
        return this;
    }

    public Config setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public Config setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    public Config setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
        return this;
    }

    public Config setTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public Config setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }
}
