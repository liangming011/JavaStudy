package agent.staticagent.example3;

public class RealSubject implements Subject {

    @Override
    public void subjectShow() {
        System.out.println("杀人是我指使的，我是幕后黑手！By---" + getClass());
    }
}
