import fiona.apple.AbstractCommandManager;
import fiona.apple.CommandManager;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import x.y.ThingOne;

public class MainTest {
    @Test
    public void mainTest() {
        System.out.printf("111");
    }

    @Test
    public void SpringTest() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("things.xml");
        ThingOne thingOne = applicationContext.getBean("beanOne", ThingOne.class);

    }

    /**
     * 1.4.6. Method Injection 方法注入
     * 在单例的 Bean 中使用其他 多例 Bean
     */
    @Test
    public void MethodInjectionTest() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("command.xml");
        CommandManager commandManager = applicationContext.getBean("commandManager", CommandManager.class);
        commandManager.process(1);
        commandManager.process(1);
        commandManager.process(1);
    }

    /**
     * Lookup Method Injection
     * Lookup Method Injection 是指容器能够覆盖容器管理的 bean 中的方法, 并返回其他名字的 bean 结果来替换原来的
     * 这种 Lookup 通常涉及到一个原型(多例) bean
     * Spring 框架通过使用 CGLIB 库的字节码生成来实现这种方法注入, 动态地生成一个覆盖该方法的子类
     */
    @Test
    public void LookupMethodInjectionTest() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("AsyncCommand.xml");
        AbstractCommandManager commandManager = applicationContext.getBean("commandManager", AbstractCommandManager.class);
        commandManager.process(1);
        commandManager.process(1);
        commandManager.process(1);
    }

}
