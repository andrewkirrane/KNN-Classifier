/**
 * Class that saves the counts of the class names identified
 */
public class ClassCount {
    private int count;
    private String className;

    public ClassCount(String className) {
        this.count = 0;
        this.className = className;
    }

    public void increase() {
        this.count++;
    }

    public void decrease() {
        this.count--;
    }

    public int getCount() {
        return this.count;
    }

    public String getClassName() {
        return this.className;
    }
}
