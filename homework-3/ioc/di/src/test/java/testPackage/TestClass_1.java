package testPackage;

import org.senla_project.annotations.Autowire;
import org.senla_project.annotations.Component;
import org.senla_project.annotations.Value;

@Component
public class TestClass_1 {
    @Value("I'm TestClass_1's string")
    String str;

    @Autowire
    public TestClass_2 testClass2;

}
