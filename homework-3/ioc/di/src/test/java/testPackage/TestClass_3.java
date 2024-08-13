package testPackage;

import org.senla_project.annotations.Autowire;
import org.senla_project.annotations.Component;
import org.senla_project.annotations.Value;

@Component
public class TestClass_3 {

    @Autowire
    TestClass_3(TestClass_1 testClass1) {
        this.testClass1 = testClass1;
    }

    public TestClass_1 testClass1;

    @Value("I amm TestClass_3's str. Hello.")
    String str;
}
