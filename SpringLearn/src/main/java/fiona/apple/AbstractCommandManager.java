package fiona.apple;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public abstract class AbstractCommandManager implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public AbstractCommandManager() {
        System.out.println("AbstractCommandManager()");
    }

    public Object process(int commandState) {
        System.out.println("AbstractCommandManager.process(int commandState)");
        // grab a new instance of the appropriate Command
        Command command = createCommand();
        // set the state on the (hopefully brand new) Command instance
        command.setState(commandState);
        return command.execute();
    }

    protected abstract Command createCommand();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("AbstractCommandManager.setApplicationContext(ApplicationContext applicationContext)");
        this.applicationContext = applicationContext;
    }
}
