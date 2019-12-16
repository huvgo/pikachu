package go.pokemon.pikachu.util;

public class TimeLogger {


    private Long startTimeMillis;

    private Integer startLineNumber;

    private Long nodeTimeMillis;

    private Integer nodeLineNumber;

    private TimeLogger() {
        startTimeMillis = System.currentTimeMillis();
        startLineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

        nodeTimeMillis = startTimeMillis;
        nodeLineNumber = startLineNumber;
    }

    public static TimeLogger start() {
        return new TimeLogger();
    }

    public void toNow() {
        var stackTrace = Thread.currentThread().getStackTrace();
        var methodName = stackTrace[2].getMethodName();
        if (methodName.startsWith("lambda")) {
            methodName = methodName.substring(methodName.indexOf("$") + 1, methodName.lastIndexOf("$"));
        }
        String className = stackTrace[2].getClassName();
        int lineNumber = stackTrace[2].getLineNumber();
        var timeEquation = System.currentTimeMillis() - nodeTimeMillis;
        System.out.println("方法性能分析：" + className + " - " + methodName + " <" + nodeLineNumber + "行-" + lineNumber + "行>" + " - 耗时：" + timeEquation + "ms");
        log();
    }

    public void total() {
        var stackTrace = Thread.currentThread().getStackTrace();
        var methodName = stackTrace[2].getMethodName();
        if (methodName.startsWith("lambda")) {
            methodName = methodName.substring(methodName.indexOf("$") + 1, methodName.lastIndexOf("$"));
        }
        String className = stackTrace[2].getClassName();
        int lineNumber = stackTrace[2].getLineNumber();
        var timeEquation = System.currentTimeMillis() - startTimeMillis;
        System.out.println("方法性能分析：" + className + " - " + methodName + " <" + startLineNumber + "行-" + lineNumber + "行>" + " - 耗时：" + timeEquation + "ms");
        log();
    }

    private void log() {
        nodeTimeMillis = System.currentTimeMillis();
        nodeLineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
    }

}
