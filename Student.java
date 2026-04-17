package mess;

import javafx.beans.property.*;

public class Student {

    private final IntegerProperty id;
    private final StringProperty name;
    private final IntegerProperty total;

    public Student(int id, String name, int total) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.total = new SimpleIntegerProperty(total);
    }

    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public int getTotal() { return total.get(); }

    public void addAmount(int amount) {
        total.set(total.get() + amount);
    }
}