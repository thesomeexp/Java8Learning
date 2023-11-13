package fiona.apple;

public class Command {

    private int state;

    public Command() {
        System.out.println("Command 被实例化");
    }

    public Object execute() {
        System.out.println(this);
        return null;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
