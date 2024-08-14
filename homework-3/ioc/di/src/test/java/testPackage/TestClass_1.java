package testPackage;

import org.senla_project.ioc.annotations.Autowire;
import org.senla_project.ioc.annotations.Component;
import org.senla_project.ioc.annotations.Value;

@Component
public class TestClass_1 {
    @Value("${testStr}")
    String str;

    @Autowire
    public TestClass_2 testClass2;

}
