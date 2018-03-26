package fyy.Serialize;

import java.io.Serializable;

/**
 * Created by fuyi on 18/3/26.
 */
public abstract class Bird implements Serializable{
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract void fly();
}
