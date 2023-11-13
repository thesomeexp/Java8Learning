import java.util.List;

/**
 * @author someexp
 * @date 2021/6/3
 */
public class EmployeeRepository {
    private List<Employee> empList;

    public EmployeeRepository(List<Employee> empList) {
        this.empList = empList;

    }

    public Employee findById(Integer id) {
        for (Employee emp : empList) {
            if (emp.getId().equals(id)) {
                return emp;
            }
        }

        return null;
    }
}
