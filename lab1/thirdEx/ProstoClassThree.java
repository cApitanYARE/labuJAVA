package samples.synchronize.tntu;

/**
 * Created by andriy on 2/6/17.
 * просто собі клас, але публічний
 * @author andriy
 * @see Lab1
 */
public class ProstoClassThree {

    // деяка змінна класу
    private int classVariable;

    /**
     * деякий конструктор класу
     *
     */
    ProstoClassThree(){
        super();
    }

    ProstoClassThree(int classVariable){
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
