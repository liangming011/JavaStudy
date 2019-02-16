package 泛型;

public class GenericParadigm2<T> {

    private T obj;

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public GenericParadigm2() {
    }

    public GenericParadigm2(T obj) {
        super();
        this.obj = obj;
    }

    public <Q extends Number> void inspect(Q q) {
        System.out.println(obj.getClass().getName());
        System.out.println(q.getClass().getName());
    }

    public static void main(String[] args) {
        GenericParadigm2<String> b = new GenericParadigm2<>();
        b.setObj("Hello");
        b.inspect(12);
        b.inspect(1.5);
    }
}
