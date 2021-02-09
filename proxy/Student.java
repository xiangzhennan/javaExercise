package proxy;

public class Student implements Person {
    private String name;
    public Student(String name){
        this.name = name;
    }

    @Override
    public void giveMoney() {
        System.out.println(name+ "pay 50 yuan");
    }

    public void ownMethod(){
        System.out.println("i am unique");
    }
}
