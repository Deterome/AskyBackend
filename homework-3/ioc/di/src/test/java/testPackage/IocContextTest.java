package testPackage;

import org.senla_project.annotations.IocContext;

public class IocContextTest {

    public static void main(String[] args) {
        IocContext context = new IocContext("testPackage");

        var testClass1 = context.findComponent("testClass_1", TestClass_1.class);
        if (testClass1 != null) {
            System.out.println("findComponent method is working");
        }
        var testClass2 = context.findComponentByType(TestClass_2.class);
        if (testClass2 != null) {
            System.out.println("findComponentByType method is working");
        }
        if (testClass1.str != null) {
            System.out.println("Value annotation is working");
            System.out.println("String of testClass_1: " + testClass1.str);
        }
        if (testClass1.testClass2 != null) {
            System.out.println("Field Autowire annotation is working");
        }
        if (testClass1.testClass2.getTestClass3() != null) {
            System.out.println("Setter Autowire annotation is working");
        }
        if (testClass1.testClass2.testClass3.testClass1 != null) {
            System.out.println("Constructor Autowire annotation is working");
        }
    }
}
