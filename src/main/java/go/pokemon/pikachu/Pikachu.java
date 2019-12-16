package go.pokemon.pikachu;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.date.DateUnit;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class Pikachu {

    //  组件相关
    private RequestQueue requestQueue;

    private Handler handler;

    private Config config = new Config();

    private Configurer configurer;

    private Downloader downloader;

    //  线程相关
    private AtomicInteger threadCount = new AtomicInteger();

    private ReentrantLock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    private ReentrantLock urlLock = new ReentrantLock();

    private Condition urlCondition = urlLock.newCondition();

    private ExecutorService executorService;

    // LRU (least recently used)最近最久未使用缓存。根据使用时间来判定对象是否被持续缓存，当对象被访问时放入缓存，当缓存满了，最久未被使用的对象将被移除。
    private Cache<String, Integer> lruCache = CacheUtil.newLRUCache(512);

    private Pikachu(RequestQueue requestQueue, Handler handler, Configurer configurer) {
        this.requestQueue = requestQueue;
        this.handler = handler;
        this.configurer = configurer;
    }

    public void go() {
        initCheck();
        log.info("Go,Pikachu!");
        while (true) {
            String url = requestQueue.poll();
            if (url == null) {
                if (threadCount.get() == 0) {
                    break;
                }
                urlLock.lock();
                try {
                    if (threadCount.get() != 0) {
                        urlCondition.await(30, TimeUnit.SECONDS);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    urlLock.unlock();
                }
            } else {
                // 当正在运行的线程数大于等于配置的线程数
                if (threadCount.get() >= config.threadNum) {
                    // 上锁
                    lock.lock();
                    try {
                        // 等待 - 直到在运行的线程数不大于等于配置的线程数
                        while (threadCount.get() >= config.threadNum) {
                            try {
                                condition.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } finally {
                        lock.unlock();
                    }
                }
                // 线程计数加一
                threadCount.incrementAndGet();
                executorService.execute(() -> {
                    try {
                        Response responseByDown = downloader.down(url, config);
                        if (responseByDown.isDone()) {
                            // 下载成功
                            PikachuContext pikachuContext = new PikachuContext();
                            pikachuContext.setResponse(responseByDown);
                            handler.handle(pikachuContext);
                            Set<String> seeds = pikachuContext.getRequest();
                            if (!(seeds == null || seeds.isEmpty())) {
                                seeds.forEach(seed -> requestQueue.push(seed));
                            }
                        } else {
                            // 没有成功，添加重试机会
                            Integer times = getByCache(url);
                            if (times == null) {
                                // 未添加过
                                requestQueue.push(url);
                                putToCache(url, 1);
                            } else if (times < 3) {
                                requestQueue.push(url);
                                putToCache(url, ++times);
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        sleep(config.sleepTime);
                        try {
                            lock.lock();
                            // 线程计数减一
                            threadCount.decrementAndGet();
                            condition.signal();
                        } finally {
                            lock.unlock();
                            try {
                                urlLock.lock();
                                urlCondition.signalAll();
                            } finally {
                                urlLock.unlock();
                            }
                        }
                    }

                });
            }
        }

        close();
    }

    private void initCheck() {
        if (configurer != null) {
            configurer.configure(config);
        }
        if (downloader == null) {
            downloader = new HttpClientDownloader();
        }
        if (executorService == null) {
            this.executorService = Executors.newFixedThreadPool(config.threadNum);
        }
    }

    private void close() {
        executorService.shutdown();
    }

    private void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            log.error("Thread interrupted when sleep", e);
        }
    }

    private synchronized void putToCache(String key, Integer value) {
        lruCache.put(key, value, DateUnit.SECOND.getMillis() * 3);
    }

    private synchronized Integer getByCache(String key) {
        return lruCache.get(key);
    }

    public static Pikachu.CrawlerBuilder builder() {
        return new Pikachu.CrawlerBuilder();
    }

    public static class CrawlerBuilder {
        private RequestQueue requestQueue = new RequestQueue();

        private Handler handler;

        private Configurer configurer;

        CrawlerBuilder() {
        }

        public Pikachu.CrawlerBuilder seedRequest(String... urls) {
            for (String url : urls) {
                requestQueue.push(url);
            }
            return this;
        }

        public Pikachu.CrawlerBuilder responseHandler(Handler handler) {
            this.handler = handler;
            return this;
        }

        public Pikachu.CrawlerBuilder configurer(Configurer configurer) {
            this.configurer = configurer;
            return this;
        }

        public Pikachu build() {
            return new Pikachu(requestQueue, handler, configurer);
        }

        public String toString() {
            return "";
        }
    }
}
