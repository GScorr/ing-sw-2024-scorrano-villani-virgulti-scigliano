package it.polimi.ingsw.RMI_FINAL;

public class Wrapper {
    public Object obj1;
    public Object obj2;
    public Object obj3;
    public Object obj4;



    public Wrapper() {
    }
    public Wrapper(Object obj1) {
        this.obj1 = obj1;
    }

    public Wrapper(Object obj1, Object obj2) {
        this.obj1 = obj1;
        this.obj2 = obj2;
    }

    public Wrapper(Object obj1, Object obj2, Object obj3) {
        this.obj1 = obj1;
        this.obj2 = obj2;
        this.obj3 = obj3;
    }

    public Wrapper(Object obj1, Object obj2, Object obj3, Object obj4) {
        this.obj1 = obj1;
        this.obj2 = obj2;
        this.obj3 = obj3;
        this.obj4 = obj4;
    }
}
