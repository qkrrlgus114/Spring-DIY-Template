package study.reflection;

public class Loopers {

    private String name;
    private int age;

    public Loopers() {
    }

    public Loopers(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @PrintView
    public void printView() {
        System.out.println("루퍼스 정보를 출력 합니다.");
    }

    public String testGetName() {
        return "test : " + name;
    }

    public String testGetAge() {
        return "test : " + age;
    }
}
