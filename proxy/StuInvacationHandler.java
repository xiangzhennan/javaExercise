package proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class StuInvacationHandler<T> implements InvocationHandler {
    T target;

    public StuInvacationHandler(T target){
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("proxy call "+method.getName());
        MonitorUtil.start();
        Object result = method.invoke(target, args);
        MonitorUtil.finish(method.getName());
        return result;
    }
}
