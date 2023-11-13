import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertEquals;

/**
 * 参考教程地址: https://stackify.com/streams-guide-java-8/
 *
 * @author someexp
 * @date 2021/6/3
 */
public class StreamTest {

    private static final Logger logger = LoggerFactory.getLogger(StreamTest.class);

    // region 1. Stream 的创建
    @Test
    public void createStream() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        // 1.1 从一个存在的数组获取
        Stream.of(arrayOfEmps);

        // 1.2 从一个存在的 list 获取
        List<Employee> empList = Arrays.asList(arrayOfEmps);
        empList.stream();

        // 1.3 我们还可以通过独立的对象获取 Stream
        Stream.of(arrayOfEmps[0], arrayOfEmps[1], arrayOfEmps[2]);

        // 1.4 或者简单点使用 Stream.builder()
        Stream.Builder<Employee> empStreamBuilder = Stream.builder();

        empStreamBuilder.accept(arrayOfEmps[0]);
        empStreamBuilder.accept(arrayOfEmps[1]);
        empStreamBuilder.accept(arrayOfEmps[2]);

        Stream<Employee> empStream = empStreamBuilder.build();
    }
    // endregion

    // region 2. Java Stream 流操作

    /**
     * forEach() 将对每个在 empList 的对象有效地调用 salaryIncrement() 方法
     * forEach() 是一个终端操作(terminal operation), 也就意味着在执行后, 流管道被认为是被消耗了, 不能再被使用
     */
    @Test
    public void whenIncrementSalaryForEachEmployee_thenApplyNewSalary() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);
        empList.stream().forEach(e -> e.salaryIncrement(10.0));

        assertThat(empList, contains(
                hasProperty("salary", equalTo(110000.0)),
                hasProperty("salary", equalTo(220000.0)),
                hasProperty("salary", equalTo(330000.0))
        ));
    }

    /**
     * map() 能产生新的 Stream, 而这个 Stream 的类型可能会不一样
     * 下面的例子是将 Integer 的 Stream 转换成 Employee
     * 下面的例子中每个 Integer 传递进 employeeRepository::findById(), 该方法返回对应的 Employee 对象, 非常高效的形成 Employee 流
     */
    @Test
    public void whenMapIdToEmployees_thenGetEmployeeStream() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);
        EmployeeRepository employeeRepository = new EmployeeRepository(empList);
        Integer[] empIds = {1, 2, 3};

        List<Employee> employees = Stream.of(empIds)
                .map(employeeRepository::findById)
                .collect(Collectors.toList());

        assertEquals(employees.size(), empIds.length);
    }

    /**
     * collect(), 我们在之前的例子中看到了 collect() 如何工作, 在我们完成所有处理后, 它是将东西从 Stream 中取出的常见方式之一
     * collect() 对 Stream 实例中持有的数据元素进行可变的折叠操作(将元素重新包装成一些数据结构, 并应用一些额外的逻辑, 将它们连接起来等等)
     * 这个操作的策略是根据 Collector 接口的实现提供的. 下面的例子中我们使用 toList collector 来收集所有 Stream 元素进 List 实例.
     */
    @Test
    public void whenCollectStreamToList_thenGetList() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);
        List<Employee> employees = empList.stream().collect(Collectors.toList());

        assertEquals(empList, employees);
    }

    /**
     * filter
     * filter() 将从原始流中通过给定测试的(元素)中产生新的流
     * 下面的例子中, 首先 filter 过滤掉那些从 employee id 中得到的 null 引用, 然后使用工资过滤
     */
    @Test
    public void whenFilterEmployees_thenGetFilteredStream() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);
        EmployeeRepository employeeRepository = new EmployeeRepository(empList);
        Integer[] empIds = {1, 2, 3, 4};

        List<Employee> employees = Stream.of(empIds)
                .map(employeeRepository::findById)
                .filter(e -> e != null)
                .filter(e -> e.getSalary() > 200000)
                .collect(Collectors.toList());

        assertEquals(Arrays.asList(arrayOfEmps[2]), employees);
    }

    /**
     * findFirst() 返回一个 Stream 流中的首个 Optional 对象(可 empty)
     * 下面的例子中, 会返回首个工资大于 100000 的雇员, 如果没有这个雇员就返回 null
     */
    @Test
    public void whenFindFirst_thenGetFirstEmployeeInStream() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);
        EmployeeRepository employeeRepository = new EmployeeRepository(empList);
        Integer[] empIds = {1, 2, 3, 4};

        Employee employee = Stream.of(empIds)
                .map(employeeRepository::findById)
                .filter(e -> e != null)
                .filter(e -> e.getSalary() > 100000)
                .findFirst()
                .orElse(null);

        assertEquals(employee.getSalary(), new Double(200000));
    }

    /**
     * toArray()
     * 上面我们使用 collect() 来从 Stream 中获得数据, 如果我们需要获得数组, 那么我们可以简单的调用 toArray()
     * 语法 Employee[]::new  将为 Employee 创建一个空数组, 然后使用流中的元素填充进去
     */
    @Test
    public void whenStreamToArray_thenGetArray() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);
        Employee[] employees = empList.stream().toArray(Employee[]::new);

        assertThat(empList.toArray(), equalTo(employees));
    }

    /**
     * flatMap
     * 一个流中可以包含一些复杂的对象, 比如 Stream<List<String>>. 为了预防这种情况, flatMap() 帮助我们去压平(flatten)这种数据结构来简化之后的操作
     * 在下面的例子中, 它使用 flatMap() 来将 Stream<List<String>> 转化为简单的 Stream<String> 类型
     */
    @Test
    public void whenFlatMapEmployeeNames_thenGetNameStream() {
        List<List<String>> namesNested = Arrays.asList(
                Arrays.asList("Jeff", "Bezos"),
                Arrays.asList("Bill", "Gates"),
                Arrays.asList("Mark", "Zuckerberg"));

        List<String> namesFlatStream = namesNested.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        assertEquals(namesFlatStream.size(), namesNested.size() * 2);
    }

    /**
     * peek
     * 之前的章节中我们看了 forEach() 这个 终阶段 操作. 然而, 有时候我们需要在任何 终阶段 操作前对每个流中的元素执行多种操作.
     * (在这里说的 终阶段 操作就是返回 void 了, 无法再进行操作了)
     * peek() 可以用于这种情况. 简单地说, 它对流中每个元素执行指定的操作, 并返回一个可以进一步使用的新流. peek() 是一个 中间操作.
     */
    @Test
    public void whenIncrementSalaryUsingPeek_thenApplyNewSalary() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };

        List<Employee> empList = Arrays.asList(arrayOfEmps);

        empList.stream()
                .peek(e -> e.salaryIncrement(10.0))
                .peek(System.out::println)
                .collect(Collectors.toList());

        assertThat(empList, contains(
                hasProperty("salary", equalTo(110000.0)),
                hasProperty("salary", equalTo(220000.0)),
                hasProperty("salary", equalTo(330000.0))
        ));
    }
    // endregion

    // region 3. 方法类型 和 管道

    /**
     * 正如我们讨论的那样, Java 流操作分为 终阶段 和 中阶段 操作.
     * stream pipeline = [stream source] + [zero or more intermediate operations] + [terminal operation]
     * 下面是一个简单的流管道, 由 empList 作为流的源, filter() 作为中阶段操作, 以及 count() 作为 终阶段操作.
     */
    @Test
    public void whenStreamCount_thenGetElementCount() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };

        List<Employee> empList = Arrays.asList(arrayOfEmps);

        Long empCount = empList.stream()
                .filter(e -> e.getSalary() > 200000)
                .count();

        assertEquals(empCount, new Long(1));
    }

    /**
     * 一些操作被视为 短路操作
     * 短路操作允许在有限的时间内完成无限流的计算
     * 下面的例子中我们使用短路操作 skip() 来跳过前面的 3 个元素, 然后使用 limit() 来从 iterate() 生成的无限流中限制 5 个元素.
     */
    @Test
    public void whenLimitInfiniteStream_thenGetFiniteElements() {
        Stream<Integer> infiniteStream = Stream.iterate(2, i -> i * 2);

        List<Integer> collect = infiniteStream
                .skip(3)
                .limit(5)
                .collect(Collectors.toList());

        assertEquals(collect, Arrays.asList(16, 32, 64, 128, 256));
    }
    // endregion

    // region 4. 延迟计算 Lazy Evaluation

    /**
     * Java stream 流一个最重要的特点是, 它允许通过 延迟计算 来进行意义非凡的优化
     * 对源数据的计算只在终端操作启动时进行, 而源元素只在需要时被消耗
     * 所有 中阶段 操作都是延迟的, 所以在实际需要处理结果之前不会执行
     * <p>
     * 例如, 思考一下下面的 findFirst() 例子. map() 操作在这里执行了多少次? 4 次? 因为输入数组包含 4 个元素?
     * 下面的例子中:
     * 流执行 map 和两次 filter 操作, 一次一个元素
     * 1. 它首先对 id 1 执行所有操作. 因为 id 1 的薪水不大于 100000, 因此处理操作就处理下一个元素
     * 2. id 2 满足了两个 filter, 因此流执行了 终阶段 操作 findFirst() 并返回结果.
     * 在上面的流程中, 对 id 为 3, 4 的并没有操作.
     * 懒惰的处理流, 可以避免在没有必要时检查所有的数据. 当输入流是无限的, 而不仅仅是非常大的时候, 这种行为变得更加重要.
     */
    @Test
    public void whenFindFirst_thenGetFirstEmployeeInStream2() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);
        EmployeeRepository employeeRepository = new EmployeeRepository(empList);
        Integer[] empIds = {1, 2, 3, 4};

        Employee employee = Stream.of(empIds)
                .map(employeeRepository::findById)
                .filter(e -> e != null)
                .filter(e -> e.getSalary() > 100000)
                .findFirst()
                .orElse(null);

        assertEquals(employee.getSalary(), new Double(200000));
    }
    // endregion

    // region 5. 基于 Stream 操作的比较

    /**
     * sorted 排序
     * 下面的流排序是基于我们传入的 comparator
     * 例如我们可以根据雇员的名字给他们排序
     * 注: 短路 short-circuiting 不会被应用于 sorted(). 这就意味着, 如果上面的例子中在 sorted() 后面使用了 findFirst(), 在 findFirst()
     * 之前, 对所有元素的排序操作就完成了. 因为不遍历整个流是不知道谁是第一个元素的.
     */
    @Test
    public void whenSortStream_thenGetSortedStream() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);

        List<Employee> employees = empList.stream()
                .sorted((e1, e2) -> e1.getName().compareTo(e2.getName()))
                .collect(Collectors.toList());

        assertEquals(employees.get(0).getName(), "Bill Gates");
        assertEquals(employees.get(1).getName(), "Jeff Bezos");
        assertEquals(employees.get(2).getName(), "Mark Zuckerberg");
    }

    /**
     * min and max 最小值 和 最大值
     * min() 和 max() 基于 comparator 返回最小值和最大值. 它们会返回一个 Optional 因为结果可能不存在. (由于 say, filter)
     */
    @Test
    public void whenFindMin_thenGetMinElementFromStream() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);

        Employee firstEmp = empList.stream()
                .min((e1, e2) -> e1.getId() - e2.getId())
                .orElseThrow(NoSuchElementException::new);

        assertEquals(firstEmp.getId(), new Integer(1));
    }

    /**
     * 我们也可以通过使用 Comparator.comparing() 来避免定义比较逻辑
     */
    @Test
    public void whenFindMax_thenGetMaxElementFromStream() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);

        Employee maxSalEmp = empList.stream()
                .max(Comparator.comparing(Employee::getSalary))
                .orElseThrow(NoSuchElementException::new);

        assertEquals(maxSalEmp.getSalary(), new Double(300000.0));
    }

    /**
     * distinct
     * distinct() 不接收任何参数, 它返回流中不重复的内容, 也就是消除流中重复的内容
     * 它使用 equals() 方法对每个元素进行比较
     */
    @Test
    public void whenApplyDistinct_thenRemoveDuplicatesFromStream() {
        List<Integer> intList = Arrays.asList(2, 5, 3, 2, 4, 3);
        List<Integer> distinctIntList = intList.stream().distinct().collect(Collectors.toList());

        assertEquals(distinctIntList, Arrays.asList(2, 5, 3, 4));
    }

    /**
     * allMatch, anyMatch, and noneMatch
     * 上面这些操作都接收一个 predicate 和返回一个 boolean. 一旦决定了会应用短路操作.
     * allMatch() 检查是否所有元素对于给定 predicate 都是 true, 一旦遇到 false 就停止处理. (下面的例子中它遇到 5 就立即返回 false)
     * anyMatch() 检查流中是否有一个符合给定 predicate, 一旦遇到就是 true (下面的例子中应用了短路, 也因为首个 2 符合就返回 true)
     * noneMatch() 检查流中是否所有元素都不符合给定 predicate. (例子中当它遇到 6 就返回 false)
     */
    @Test
    public void whenApplyMatch_thenReturnBoolean() {
        List<Integer> intList = Arrays.asList(2, 4, 5, 6, 8);

        boolean allEven = intList.stream().allMatch(i -> i % 2 == 0);
        boolean oneEven = intList.stream().anyMatch(i -> i % 2 == 0);
        boolean noneMultipleOfThree = intList.stream().noneMatch(i -> i % 3 == 0);

        assertEquals(allEven, false);
        assertEquals(oneEven, true);
        assertEquals(noneMultipleOfThree, false);
    }
    // endregion

    // region 6. Java 流的专业分化

    /**
     * 从我们到目前的讨论的内容来看, Stream 是一个对象引用流.
     * 然而还有 IntStream, LongStream, DoubleStream 分别是代表 int, long, double 的分化
     * 在处理大量基于数字时提供了便利.
     *
     * 这些专门的 Stream 并不扩展 Stream 类, 而是扩展 BaseStream, Stream 也是建立在 BaseStream 之上的
     * 因此, 并非所有 Stream 的支持都出现在这些流的实现当中. 例如标准的 min() 和 max() 需要一个 comparator, 而那些专门的流则不需要
     */

    // 创建

    /**
     * 最常用创建 IntStream 的方式是在一个已经存在的流中调用 mapToInt()
     */
    @Test
    public void whenFindMaxOnIntStream_thenGetMaxInteger() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);

        Integer latestEmpId = empList.stream()
                .mapToInt(Employee::getId)
                .max()
                .orElseThrow(NoSuchElementException::new);

        assertEquals(latestEmpId, new Integer(3));

        // 我们也可以使用 IntStream.of() 来创建 IntStream
        IntStream.of(1, 2, 3);
        // 或者 IntStream.range(), 这会创建数字 10 到 19
        IntStream.range(10, 20);

        // !!! 在进入下一章前需要注意一个重要的区别 !!!
        // !!! 这样创建的返回值是 Stream<Integer> 而不是 IntStream !!!
        Stream.of(1, 2, 3);
        // 同样, 使用 map() 而不是 mapToInt() 返回值是 Stream<Integer> 类型而不是 IntStream
        empList.stream().map(Employee::getId);
    }

    // 专用的操作

    /**
     * 分化后的流对标准流提供了额外的操作, 这些操作在处理数字时非常方便.
     * 例如: sum(), average(), range() 等
     */
    @Test
    public void whenApplySumOnIntStream_thenGetSum() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);

        Double avgSal = empList.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElseThrow(NoSuchElementException::new);

        assertEquals(avgSal, new Double(200000));
    }
    // endregion

    // region 7. Reduction Operations 折叠操作

    /**
     * 缩减操作(也被称为折叠)通过重复应用组合操作, 将一连串的输入元素组合成一个单一的汇总结果.
     * 我们已经看到了一些折叠操作, 如 findFirst(), min() 和 max()
     */

    // reduce

    /**
     * 最常见的 reduce() 形式是:
     * T reduce(T identity, BinaryOperator<T> accumulator)
     * 其中身份是起始值, 累加器是我们重复应用的二进制运算
     * 例如: 我们从初始值 0 开始对流中的每个元素重复执行 Double::sum()
     * 实际上, 我们通过对 Stream 应用 reduce 实现了 DoubleStream.sum()
     */
    @Test
    public void whenApplyReduceOnStream_thenGetValue() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);

        Double sumSal = empList.stream()
                .map(Employee::getSalary)
                .reduce(0.0, Double::sum);

        assertEquals(sumSal, new Double(600000));
    }
    // endregion

    // region 8. Advanced collect 高级集合操作

    /**
     * 我们已经看过如何使用 Collectors.toList() 来从流中得到 list.
     * 下面就看一下一些从流中获取元素的其它方式
     */

    // endregion

    // region 8.1 joining 加分隔符

    /**
     * Collectors.joining() 会在流中的 String 元素之间插入分隔符. 它内部使用 java.util.StringJoiner 来执行连接操作
     */
    @Test
    public void whenCollectByJoining_thenGetJoinedString() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);

        String empNames = empList.stream()
                .map(Employee::getName)
                .collect(Collectors.joining(", "))
                .toString();

        assertEquals(empNames, "Jeff Bezos, Bill Gates, Mark Zuckerberg");
    }
    // endregion

    // region 8.2 toSet

    /**
     * 我们也可以使用 toSet() 来从流中获取元素
     */
    @Test
    public void whenCollectBySet_thenGetSet() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);

        Set<String> empNames = empList.stream()
                .map(Employee::getName)
                .collect(Collectors.toSet());

        assertEquals(empNames.size(), 3);
    }
    // endregion

    // region 8.3 toCollection

    /**
     * 我们可以使用 Collectors.toCollection() 来取出元素并放入任何传入 Supplier<Collection> 的集合中. 我们也可以对 Supplier 使用构造器引用.
     * 下面的例子中创建了一个空的集合, 然后对流中的每个元素调用了 add() 方法
     */
    @Test
    public void whenToVectorCollection_thenGetVector() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);

        Vector<String> empNames = empList.stream()
                .map(Employee::getName)
                .collect(Collectors.toCollection(Vector::new));

        assertEquals(empNames.size(), 3);
    }
    // endregion

    // region 8.4 summarizingDouble (总结 Double)

    /**
     * summarizingDouble() 是另一个有趣的 collector, 它对每个输入元素应用一个双生映射函数, 并返回一个包含结果值的统计信息的特殊类
     */
    @Test
    public void whenApplySummarizing_thenGetBasicStats() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);

        DoubleSummaryStatistics stats = empList.stream()
                .collect(Collectors.summarizingDouble(Employee::getSalary));

        assertEquals(stats.getCount(), 3);
        assertEquals(stats.getSum(), 600000.0, 0);
        assertEquals(stats.getMin(), 100000.0, 0);
        assertEquals(stats.getMax(), 300000.0, 0);
        assertEquals(stats.getAverage(), 200000.0, 0);
    }

    /**
     * 当我们用分化的流时, summaryStatistics() 可以被用来生成类似的结果
     */
    @Test
    public void whenApplySummaryStatistics_thenGetBasicStats() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);

        DoubleSummaryStatistics stats = empList.stream()
                .mapToDouble(Employee::getSalary)
                .summaryStatistics();

        assertEquals(stats.getCount(), 3);
        assertEquals(stats.getSum(), 600000.0, 0);
        assertEquals(stats.getMin(), 100000.0, 0);
        assertEquals(stats.getMax(), 300000.0, 0);
        assertEquals(stats.getAverage(), 200000.0, 0);
    }
    // endregion

    // region 8.5 partitioningBy 分组 (两组)

    /**
     * 我们可以将一个流分成两块 (基于这些元素是否满足某些 criteria)
     * 让我们把我们的数字数据清单, 分成偶数和正数
     * 下面的例子中流根据基数 (false) 还是偶数 (true) 被分进了一个 Map
     */
    @Test
    public void whenStreamPartition_thenGetMap() {
        List<Integer> intList = Arrays.asList(2, 4, 5, 6, 8);
        Map<Boolean, List<Integer>> isEven = intList.stream().collect(
                Collectors.partitioningBy(i -> i % 2 == 0));

        assertEquals(isEven.get(true).size(), 4);
        assertEquals(isEven.get(false).size(), 1);
    }
    // endregion

    // region 8.6 groupingBy 分组 (多组)

    /**
     * groupingBy() 提供更高级的分区, 我们可以将它分进更多的组
     * 它需要一个分类函数作为它的参数, 这个分类函数被应用于流的每个元素
     * 分类函数中的返回值将被用来作为 collector 中返回的 map 的 key
     * 在这个例子中, 我们将基于雇员的名字的首个字母分组
     */
    @Test
    public void whenStreamGroupingBy_thenGetMap() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);

        Map<Character, List<Employee>> groupByAlphabet = empList.stream().collect(
                Collectors.groupingBy(e -> new Character(e.getName().charAt(0))));

        assertEquals(groupByAlphabet.get('B').get(0).getName(), "Bill Gates");
        assertEquals(groupByAlphabet.get('J').get(0).getName(), "Jeff Bezos");
        assertEquals(groupByAlphabet.get('M').get(0).getName(), "Mark Zuckerberg");
    }
    // endregion

    // region 8.7 mapping (值转换)

    /**
     * 上面的例子中我们使用 Map 将流中的元素进行了分组
     * 然而, 有时候我们可能需要将分组的数据转换为其他类型
     * 我们可以使用 mapping() 来适配 collector 到一个不一样的类型
     * 下面的例子中, mapping() 将流中的雇员映射成 id, 这些 id 仍然基于雇员名字首个 character 分组
     */
    @Test
    public void whenStreamMapping_thenGetMap() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);

        Map<Character, List<Integer>> idGroupedByAlphabet = empList.stream().collect(
                Collectors.groupingBy(e -> new Character(e.getName().charAt(0)),
                        Collectors.mapping(Employee::getId, Collectors.toList())));

        assertEquals(idGroupedByAlphabet.get('B').get(0), new Integer(2));
        assertEquals(idGroupedByAlphabet.get('J').get(0), new Integer(1));
        assertEquals(idGroupedByAlphabet.get('M').get(0), new Integer(3));
    }
    // endregion

    // region 8.8 reducing

    /**
     * 它和之前提到的 reduce() 非常像.
     * 它返回一个对输入元素执行 reduction 的 collector
     * 下面的例子中, reducing() 得到每个员工增加的薪水, 并返回总数
     * 当在一个多重 reduction 中, 或者是 groupingBy() 或是 partitioningBy() 下游中, reducing() 是最常用的.
     * 可以使用 reduce() 替换 reducing() 来执行一些简单的 reduction
     */
    @Test
    public void whenStreamReducing_thenGetValue() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);

        Double percentage = 10.0;
        Double salIncrOverhead = empList.stream().collect(Collectors.reducing(
                0.0, e -> e.getSalary() * percentage / 100, (s1, s2) -> s1 + s2));

        assertEquals(salIncrOverhead, 60000.0, 0);
    }

    /**
     * 下面的例子展示了如何在 groupingBy() 中使用 reducing()
     * 例子中我们根据雇员名字首个字母分组. 对于每个组, 我们找到最长名字的那个.
     */
    @Test
    public void whenStreamGroupingAndReducing_thenGetMap() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };
        List<Employee> empList = Arrays.asList(arrayOfEmps);

        Comparator<Employee> byNameLength = Comparator.comparing(Employee::getName);

        Map<Character, Optional<Employee>> longestNameByAlphabet = empList.stream().collect(
                Collectors.groupingBy(e -> new Character(e.getName().charAt(0)),
                        Collectors.reducing(BinaryOperator.maxBy(byNameLength))));

        assertEquals(longestNameByAlphabet.get('B').get().getName(), "Bill Gates");
        assertEquals(longestNameByAlphabet.get('J').get().getName(), "Jeff Bezos");
        assertEquals(longestNameByAlphabet.get('M').get().getName(), "Mark Zuckerberg");
    }
    // endregion

    // region 9. Parallel Streams (平行流? 并行流?)

    /**
     * 使用 并行流 的支持, 我们可以不用写任何 boilerplate code 就可以对流执行并行操作, 我们只需要指定流是并行的
     * 下面的例子中, 我们简单添加 parallel() 就能使流中的元素执行 salaryIncrement() 时并行执行
     * 当然, 如果你需要对操作的性能特征进行更多的配置, 这种功能还可以进一步调整和配置
     * 注: 与编写多线程代码的情况一样, 我们在使用并行流时需要注意一些事情:
     * 1. 我们需要确保代码是线程安全的. 如果并行执行的操作修改了共享数据, 则需要特别注意.
     * 2. 如果执行操作的顺序或在输出流中返回的顺序很重要, 我们不应该使用并行流. 例如, 像 findFirst() 这样的操作在并行流的情况下可能会产生不同的结果.
     * 3. 此外, 我们应该确保它值得执行并行操作. 了解执行操作的性能特征和整体系统的特征是非常重要的.
     */
    @Test
    public void whenParallelStream_thenPerformOperationsInParallel() {
        Employee[] arrayOfEmps = {
                new Employee(1, "Jeff Bezos", 100000.0),
                new Employee(2, "Bill Gates", 200000.0),
                new Employee(3, "Mark Zuckerberg", 300000.0)
        };

        List<Employee> empList = Arrays.asList(arrayOfEmps);

        empList.stream().parallel().forEach(e -> e.salaryIncrement(10.0));

        assertThat(empList, contains(
                hasProperty("salary", equalTo(110000.0)),
                hasProperty("salary", equalTo(220000.0)),
                hasProperty("salary", equalTo(330000.0))
        ));
    }
    // endregion

    // region 10. Infinite Streams 无限流

    /**
     * 有时, 我们可能想在元素仍然在生成时进行操作. 我们可能不会预先知道我们需要多数元素. 不像 list 或者 map, 所有的元素都已经被填充.
     * 我们可以使用无限流, 也被称为无界限流.
     * 有两种方式生成无限流
     */
    // endregion

    // region 10.1 generate

    /**
     * 当流中需要元素时, 我们会调用 Supplier 来 generate() 一个新元素
     * 下面的例子中使用 Math::random 作为 Supplier, 它返回下一个随机数.
     * 对于无限流, 我们需要提供一个最终结束操作的操作. 通常使用 limit() 来完成这种操作.
     * 在下面的例子中我们 limit 流为 5 个数字, 然后在它们生成后打印它.
     * 注: Supplier 提供的 generate() 可能是有状态的, 所以当并行处理这种流时可能不会产生相同的结果
     */
    @Test
    public void whenGenerateStream_thenGetInfiniteStream() {
        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);
    }
    // endregion

    // region 10.2 iterate

    /**
     * iterate() 需要两个参数, 一个初始值 (也叫种子元素), 还有一个是根据之前的值生成新的值的函数.
     * iterate() 在设计时是一种有状态的, 所以它可能不适合使用在并行流中.
     */
    @Test
    public void whenIterateStream_thenGetInfiniteStream() {
        Stream<Integer> evenNumStream = Stream.iterate(2, i -> i * 2);

        List<Integer> collect = evenNumStream
                .limit(5)
                .collect(Collectors.toList());

        assertEquals(collect, Arrays.asList(2, 4, 8, 16, 32));
    }
    // endregion

    // region 11. 写文件操作

    /**
     * 下面的例子中我们使用 forEach() 来将流中的每个元素写到输出流中
     */
    @Test
    public void whenStreamToFile_thenGetFile() throws IOException {
        String fileName = "C:\\Users\\YourUsername\\test.txt";
        String[] words = new String[]{
                "hello",
                "refer",
                "world",
                "level"
        };

        try (PrintWriter pw = new PrintWriter(
                Files.newBufferedWriter(Paths.get(fileName)))) {
            Stream.of(words).forEach(pw::println);
        }
    }
    // endregion

    // region 12. 读文件操作

    /**
     * 下面的例子中, Files.lines() 从文件中返回一行作为 Stream, 然后 getPalindrome() 中做进一步处理
     * getPalindrome() 处理流, 但是它感知不到流是怎么被生成的. 这也增加了代码可读性, 以及简化了单元测试.
     */
    private List<String> getPalindrome(Stream<String> stream, int length) {
        return stream.filter(s -> s.length() == length)
                .filter(s -> s.compareToIgnoreCase(
                        new StringBuilder(s).reverse().toString()) == 0)
                .collect(Collectors.toList());
    }

    @Test
    public void whenFileToStream_thenGetStream() throws IOException {
        String fileName = "C:\\Users\\YourUsername\\test.txt";
        List<String> str = getPalindrome(Files.lines(Paths.get(fileName)), 5);
        assertThat(str, contains("refer", "level"));
    }
    // endregion

}
