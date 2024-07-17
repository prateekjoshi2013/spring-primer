### SOLID pricipals

- **S : Single Responsibility Principle**

    - Every Class should have a single responsibility.
    - There should never be more than one reason for a class to change.
    - Your classes should be small. No more than a screen full of code.
    - Avoid ‘god’ classes.
    - Split big classes into smaller classes.

- **O : Open Closed Principle**

    - Your classes should be open for extension
    - But closed for modification
    - You should be able to extend a classes behavior, without modifying it.
    - Use private variables with getters and setters - ONLY when you need them.
    - Use abstract base classes

- **L : Liskov Substitution Principle**

    - Objects in a program would be replaceable with instances of their subtypes WITHOUT altering the correctness of the program.
    - Violations will often fail the “Is a” test.
    - for example:
        - A Square “Is a” Rectangle
        - However, a Rectangle “Is Not” a Square 

- **I : Interface Segregation Principle**

    - Make fine grained interfaces that are client specific
    - Many client specific interfaces are better than one “general purpose” interface
    - Keep your components focused and minimize dependencies between them
    - Notice relationship to the Single Responsibility Principle?
    - Avoid ‘god’ interfaces

- **D : Dependency Inversion Principle** 

    - Abstractions should not depend upon details
    - Details should depend upon abstractions
    - Important that higher level and lower level objects depend on the same abstract interaction
    - This is not the same as Dependency Injection - which is how objects obtain dependent objects