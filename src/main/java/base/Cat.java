package base;

public class Cat extends Animal{
    int age = 80;
    static int num = 90;
    public void eat() { System.out.println("cat eat"); }
    public static void run() { System.out.println("cat run"); }
    public void sleep() {System.out.println("cat sleep");}
    public static void main(String[] args) {
        Animal cat = new Cat();
        cat.eat(); // cat eat
        cat.run(); // animal run
        System.out.println(cat.age); // 10
        System.out.println(cat.num); // 20

        Cat cat1 = new Cat();
        Cat cat2 = new Cat();
        System.out.println(cat1.equals(cat2));
        System.out.println(cat1.hashCode());
        System.out.println(cat2.hashCode());
    }
}
