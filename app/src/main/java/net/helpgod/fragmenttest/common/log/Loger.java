package net.helpgod.fragmenttest.common.log;

/**
 *
 */
public class Loger {

    public static String callLibrary(Class<?> cls){
        boolean isCheck = false;
        StackTraceElement[] se = Thread.currentThread().getStackTrace();
        for(int i=2; i<se.length; i++) {
            StackTraceElement e = se[i];
            String simpleClass = e.getClassName();

            try {
                Class<?> trace = Class.forName(simpleClass);
                if(cls.equals(trace)){
                    isCheck = true;
                    continue;
                }
                Class superclass = trace.getSuperclass();
                while (superclass != null) {
                    trace = superclass;
                    superclass = trace.getSuperclass();
                    if(cls.equals(trace)){
                        continue;
                    }
                }
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
                continue;
            }
            if(isCheck) {
                int start = simpleClass.lastIndexOf(".") + 1;
                int end = simpleClass.lastIndexOf("$");
                if (end < start) {
                    end = simpleClass.length();
                }
                simpleClass = simpleClass.substring(start, end);
                String result = e.getClassName() + "." + e.getMethodName() + "(" + simpleClass + ".java:" + e.getLineNumber() + ")";
                return result;
            }
        }
        return "Unknown caller";
    }

    public static String callClass(Class<?> cls){
        boolean isCheck = false;
        StackTraceElement[] se = Thread.currentThread().getStackTrace();
        for(int i=0; i<se.length; i++) {
            StackTraceElement e = se[i];
            String simpleClass = e.getClassName();
            try {
                Class<?> trace = Class.forName(simpleClass);
                if(cls.equals(trace)){
                    isCheck = true;
                }
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
                continue;
            }
            if(isCheck) {
                int start = simpleClass.lastIndexOf(".") + 1;
                int end = simpleClass.lastIndexOf("$");
                if (end < start) {
                    end = simpleClass.length();
                }
                simpleClass = simpleClass.substring(start, end);
                String result = "(" + simpleClass + ".java:" + e.getLineNumber() + ")";
                return result;
            }
        }
        return " Unknown caller";
    }

    public static String callMethod(int index){
        boolean isCheck = false;
        StackTraceElement[] se = Thread.currentThread().getStackTrace();
        if(se.length >=index){
                String simpleClass = se[index].getClassName();
                int start = simpleClass.lastIndexOf(".") + 1;
                int end = simpleClass.lastIndexOf("$");
                if (end < start) {
                    end = simpleClass.length();
                }
                simpleClass = simpleClass.substring(start, end);
            String result = "(" + simpleClass + ".java:" + se[index].getLineNumber() + ")";
            return result;
        }
        return " Unknown caller";
    }
}
