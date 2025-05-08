package samples.synchronize;

/**
 * Created by andriy on 2/6/17.
 * просто собі клас, але публічний
 * @author andriy
 * @see Main
 */
public class ProstoClass {

    // деяка змінна класу
    private int classVariable;

    /**
     * деякий конструктор класу
     *
     */
    ProstoClass(){
        super();
    }

    ProstoClass(int classVariable){
        this.classVariable = classVariable;
    }
    /**
     * деякий метод
     */
    public void printVariable(){
        System.out.println("А я деякий метод, який виводить на екран змінну класу " + this.classVariable);
    }
    /**
     * деякий метод
     * @return змінну класу
     */
    public int getClassVariable(){
        return this.classVariable;
    }

}
