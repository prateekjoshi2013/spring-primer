### Spring Dependency Injection

- Dependency Injection is where a needed dependency is injected by another object.

- Can be at instantiation via constructor, or after via setter

- The class being injected has no responsibility in instantiating the object being injected.

- Some say you avoid declaring objects using ‘new’
    
    - Not 100% correct…
    - Be pragmatic in what is and is not being managed in the Spring Context

### Types of Spring Dependency Injection

- By Constructor - Most Preferred

- By Setters - Area of much debate

- By class properties - least preferred

    - Can be public or private properties

    - Using private properties is EVIL

    - Spring can use reflection to set private properties

    - “Works” but is slow & makes testing difficult

### Dependency Injection By - Concrete Classes or with Interfaces ?

- DI can be done with Concrete Classes or with Interfaces

- Generally DI with Concrete Classes should be avoided

- DI via Interfaces is highly preferred

- Allows runtime to decide implementation to inject

- Follows Interface Segregation Principle of SOLID

- Also, makes your code more testable - mocking becomes easy

### Inversion of Control - aka IoC

- Is a technique to allow dependencies to be injected at runtime

- Dependencies are not predetermined

- Allows the framework to compose the application by controlling which implementation is injected
    
    - Example - H2 in memory data source or MySQL data source 


### IoC vs Dependency Injection

- IoC and DI are easily confused
- DI refers much to the composition of your classes
    - ie you compose your classes with DI in mind
- You might write code to ‘inject’ a dependency
- IoC is the runtime environment of your code
- Control of Dependency Injection is inverted to the framework
- Spring using its Application Context is in control of the injection  of dependencies

### Best Practices for using Spring Dependency Injection

- Favor using Constructor Injection over Setter Injection
- Use final properties for injected components
- Declare property private final and initialize in the constructor
- Whenever practical, code to an interface 