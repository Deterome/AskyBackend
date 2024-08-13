package testPackage;

import org.senla_project.annotations.Autowire;
import org.senla_project.annotations.Component;
import org.senla_project.annotations.Value;

@Component
public class TestClass_2 {

    @Value("Hi! I'm TestClass_2's str!")
    String str;

    @Autowire
    public void setTestClass3(TestClass_3 testClass3) {
        this.testClass3 = testClass3;
    }

    public TestClass_3 getTestClass3() {
        return testClass3;
    }

    TestClass_3 testClass3;

}
