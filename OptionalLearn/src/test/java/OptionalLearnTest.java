import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author someexp
 * @date 2021/6/1
 */
public class OptionalLearnTest {

    private static final Logger logger = LoggerFactory.getLogger(OptionalLearnTest.class);

    // region 创建 Optional 实例

    /**
     * 创建一个空的 Optional 对象
     */
    @Test(expected = NoSuchElementException.class)
    public void whenCreateEmptyOptional_thenNull() {
        Optional<User> emptyOpt = Optional.empty();
        emptyOpt.get();
    }

    /**
     * 你可以使用 of() / ofNullable() 来创建 Optional 对象
     * 区别是如果传入为 null, of() 会抛出异常
     */
    @Test(expected = NullPointerException.class)
    public void whenCreateOfEmptyOptional_thenNullPointerException() {
        Optional<User> opt = Optional.of(null);
    }
    // endregion

    // region 访问 Optional 对象中的值

    /**
     * 使用 get(), 如果为 null 也会抛出异常
     */
    @Test
    public void whenCreateOfNullableOptional_thenOk() {
        String name = "John";
        Optional<String> opt = Optional.ofNullable(name);

        assertEquals("John", opt.get());
    }

    /**
     * 为了避免 get() 的异常, 你可以选择首先验证值是否存在
     */
    @Test
    public void whenCheckIfPresent_thenOk() {
        User user = new User("john@gmail.com", "1234");
        Optional<User> opt = Optional.ofNullable(user);
        assertTrue(opt.isPresent());

        assertEquals(user.getEmail(), opt.get().getEmail());
    }

    /**
     * 另外一种检查值是否存在的方式是使用 ifPresent()
     * 它还接受一个 Consumer 参数, 如果对象不是空的就执行 lambda 表达式
     */
    @Test
    public void whenCheckIfPresent_thenOk2() {
        User user = new User("john@gmail.com", "1234");
        Optional<User> opt = Optional.ofNullable(user);

        opt.ifPresent(u -> assertEquals(u.getEmail(), u.getEmail()));
    }

    /**
     * 为 null 时返回默认值
     */
    @Test
    public void whenEmptyValue_thenReturnDefault() {
        User user = null;
        User user2 = new User("anna@gmail.com", "1234");
        User result = Optional.ofNullable(user).orElse(user2);

        assertEquals(user2.getEmail(), result.getEmail());
    }

    /**
     * 不为 null 时不返回默认值
     */
    @Test
    public void whenValueNotNull_thenIgnoreDefault() {
        User user = new User("john@gmail.com", "1234");
        User user2 = new User("anna@gmail.com", "1234");
        User result = Optional.ofNullable(user).orElse(user2);

        assertEquals("john@gmail.com", result.getEmail());
    }

    /**
     * 为 null 时执行相关代码然后返回默认值
     */
    @Test
    public void whenValueNull_thenExecuteFunction() {
        User user = null;
        User user2 = new User("anna@gmail.com", "1234");
        User result = Optional.ofNullable(user).orElseGet(() -> user2);

        assertEquals("anna@gmail.com", result.getEmail());
    }
    // endregion

    // region orElse() 和 orElseGet() 区别

    /**
     * 如果提供的对象为 null, 那这两者没啥不一样的行为
     */
    @Test
    public void givenEmptyValue_whenCompare_thenOk() {
        User user = null;
        logger.info("Using orElse");
        User result = Optional.ofNullable(user).orElse(createNewUser());
        logger.info("Using orElseGet");
        User result2 = Optional.ofNullable(user).orElseGet(() -> createNewUser());
    }

    private User createNewUser() {
        logger.debug("Creating New User");
        return new User("extra@gmail.com", "1234");
    }

    /**
     * 如果是下面这种情况, user 为非 null 的情况
     * orElse() 仍然会创建一个默认的 User 对象, 与此相反, orElseGet() 不会创建一个 User 对象
     */
    @Test
    public void givenPresentValue_whenCompare_thenOk() {
        User user = new User("john@gmail.com", "1234");
        logger.info("Using orElse");
        User result = Optional.ofNullable(user).orElse(createNewUser());
        logger.info("Using orElseGet");
        User result2 = Optional.ofNullable(user).orElseGet(() -> createNewUser());
    }
    // endregion

    // region 返回一个异常

    /**
     * 和 orElseGet() 差不多, 如果为 null, orElseThrow() 将抛出一个异常
     */
    @Test(expected = IllegalArgumentException.class)
    public void whenThrowException_thenOk() {
        User user = null;
        User result = Optional.ofNullable(user)
                .orElseThrow(() -> new IllegalArgumentException());
    }
    // endregion

    // region 值转换

    /**
     * 使用 map() 来对值进行转换
     * map() 传入 function 参数, 然后将结果包装在 Optional, 因此可以进行一系列链操作
     */
    @Test
    public void whenMap_thenOk() {
        User user = new User("anna@gmail.com", "1234");
        String email = Optional.ofNullable(user)
                .map(u -> u.getEmail()).orElse("default@gmail.com");

        assertEquals(email, user.getEmail());
    }

    /**
     * map() 与 flatMap() 的比较
     * flatMap() 同样传入 function 参数以及直接返回结果
     * 而其返回值是解包后的字符串对象, 和 map() 不一样的是它不会再对返回值封装成 Optional
     */
    @Test
    public void whenFlatMap_thenOk() {
        User user = new User("anna@gmail.com", "1234");
        user.setPosition("Developer");
        String position = Optional.ofNullable(user)
                .flatMap(u -> u.getPosition()).orElse("default");

        assertEquals(position, user.getPosition().get());
    }
    // endregion

    // region 值过滤

    /**
     * filter() 传入 Predicate 参数, 如果返回结果为 true 则返回值, 否则返回空 Optional 对象
     */
    @Test
    public void whenFilter_thenOk() {
        User user = new User("anna@gmail.com", "1234");
        Optional<User> result = Optional.ofNullable(user)
                .filter(u -> u.getEmail() != null && u.getEmail().contains("@"));

        assertTrue(result.isPresent());
    }
    // endregion

    // region 链接 Optional 类的方法

    /**
     * 现在可以去除 null 判断, 使用 Optional 代替
     */
    @Test
    public void whenChaining_thenOk() {
        User user = new User("anna@gmail.com", "1234");

        String result = Optional.ofNullable(user)
                .flatMap(u -> u.getAddress())
                .flatMap(a -> a.getCountry())
                .map(c -> c.getIsocode())
                .orElse("default");

        assertEquals(result, "default");
    }

    /**
     * 上面的代码可以使用方法引用进一步减少代码量
     */
    @Test
    public void whenChaining_thenOk2() {
        User user = new User("anna@gmail.com", "1234");

        String result = Optional.ofNullable(user)
                .flatMap(User::getAddress)
                .flatMap(Address::getCountry)
                .map(Country::getIsocode)
                .orElse("default");

        assertEquals(result, "default");
    }
    // endregion

    // region Optional 应该如何被使用?
    // 我们需要讨论一下下这个 Optional 应该怎么用
    // 1. 一个重要的点是, Optional 不是 Serializable 的对象, 因此它应该不是设计来作为一个字段类型的.
    // 如果你非要序列化一个 Optional 对象, Jackson 库也提供了一些库支持对 Optional 序列化, 具体还是自己去看吧.
    // 2. Optional 不应该作为一个参数使用, 比方说你把 Optional 作为参数传入一个构造方法, 它不比创建重载的构造方法更有效.
    // 3. Optional 的预期用途是作为一个返回类型. 在获取了一个对象之后, 如果值存在可以取值, 否则提供一个默认值.

    /**
     * 一个非常实用的实例是, 使用 Optional 类的 stream() 或者其它返回 Optional 类型的方法来构造 fluent APIs.
     */
    @Test
    public void whenEmptyStream_thenReturnDefaultOptional() {
        List<User> users = new ArrayList<>();
        User user = users.stream().findFirst().orElse(new User("default", "1234"));

        assertEquals(user.getEmail(), "default");
    }
    // endregion

}
