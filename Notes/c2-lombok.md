### lombok

- Project Lombok provides annotations which help eliminate the writing of ceremonial code

- Using Project Lombok will save you time and give you cleaner code

#### How lombok works ?

- Hooks in via the Annotation processor API

- The AST (raw source code) is passed to Lombok for code generation before java continues.

- Thus, produces properly compiled Java code in conjunction with the Java compiler
    
    - NOTE: Code is generated and complied. No run-time performance penalty

#### Overriding Lombok implementation 

- If you write an implantation of a method Project Lombok would generate, your code is used

    - Make it easy to override Lombok generated code

### Annotaions

- val 
    
    - declares final local variable

- var 
    
    - declares mutable local variable

- @Getter 
    
    - Creates getter methods for all properties

- @Setter 
    
    - Creates setter for all non-final properties

- @ToString
    
    - Generates String of classname, and each field separated by commas

    - Optional parameter to include field names

    - Optional parameter to include call to the super toString method

- @EqualsAndHashCode

    - Generates implementations of ‘equals(Object other) and hashCode()

    - By default will use all non-static, non-transient properties

    - Can optionally exclude specific properties

- @NoArgsConstructor

    - Generates no args constructor

    - Will cause compiler error if there are final fields

    - Can optionally force, which will initialize final fields with 0 / false / null

- @RequiredArgsContructor

    - Generates a constructor for all fields that are final or marked @NonNull

    - Constructor will throw a NullPointerException if any @NonNull fields are null 

- @Data

    - Generates typical boilerplate code for POJOs

    - Combines - @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor
    
    - No constructor is generated if constructors have been explicitly declared

- @Value

    - The immutable variant of @Data

    - All fields are made private and final by default

- @NonNull

    - Set on parameter of method or constructor and a NullPointerException will be thrown if parameter is null


- •@Builder
  
    - Implements the ‘builder’ pattern for object creation
  
    ```java
        Person
        .builder()
        .name("Adam Savage")
        .city("San Francisco")
        .job("Mythbusters")
        .job("Unchained Reaction").build();
    ```

- @SneakyThrows
    
    - Throw checked exceptions without declaring in calling method’s throws clause

- @Syncronized
    
    - A safer implementation of Java’s synchronized

- @Log

    - Creates a Java util logger

    - Java util loggers are awful

- @Slf4j (Recommended)

    - Creates a SLF4J logger.

    - SLF4J is a generic logging facade

    - Spring Boot’s default logger is LogBack


#### IDE plugins

- Since compiled code is change, and source files are not, IDE’s can get confused by this.
- More of an issue for IDEs several years old.
- Modern IDEs such as IntelliJ, Eclipse (and offshoots), Netbeans support Project Lombok
- Plugin Installation may be necessary

•Example: custom setters
- @Getter - generates getters
- @Setter - generates setters