package fiona.apple;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class CommandManager implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public CommandManager() {
        System.out.println("CommandManager()");
    }
    
    public Object process(int commandState) {
        System.out.println("CommandManager.process(int commandState)");
        // grab a new instance of the appropriate Command
        Command command = createCommand();
        // set the state on the (hopefully brand new) Command instance
        command.setState(commandState);
        return command.execute();
    }

    protected Command createCommand() {
        System.out.println("CommandManager.createCommand()");
        // notice the Spring API dependency!
        return this.applicationContext.getBean("command", Command.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("CommandManager.setApplicationContext(ApplicationContext applicationContext)");
        this.applicationContext = applicationContext;
    }
}
