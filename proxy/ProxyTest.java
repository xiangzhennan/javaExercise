package proxy;

import java.lang.reflect.Proxy;

public class ProxyTest {
    public static void main(String[] args) {
        Student student = new Student("xzn");
        StuInvacationHandler<Person> stuHandler = new StuInvacationHandler<>(student);

        Person proxy = (Person) Proxy.newProxyInstance(Person.class.getClassLoader(), new Class[]{Person.class}, stuHandler);
    }
}
