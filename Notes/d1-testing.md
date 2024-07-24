### Unit Tests / Unit Testing

- Unit Tests / Unit Testing - Code written to test code under test

- Designed to test specific sections of code

- Percentage of lines of code tested is code coverage

- Ideal coverage is in the 70-80% range

- Should be ‘unity’ and execute very fast

- Should have no external dependencies

- ie no database, no Spring context, etc

### Integration Tests

- Integration Tests - Designed to test behaviors between objects and parts of the overall system

- Much larger scope

- Can include the Spring Context, database, and message brokers

- Will run much slower than unit tests

### Functional Tests 

- Typically means you are testing the running application

- Application is live, likely deployed in a known environment

- Functional touch points are tested - (i.e. Using a web driver, calling web services, sending / receiving messages, etc)


### Mockito

- Mockito is the most popular mocking framework for testing Java

- Mocks (aka Test Doubles) are alternate implementations of objects to replace real objects in tests

- Works well with Dependency Injection

- For the class under test, injected dependencies can be mocked

#### Types of Mocks (aka Test Doubles)

- Dummy - Object used just to get the code to compile

- Fake - An object that has an implementation, but not production ready

- Stub - An object with pre-defined answers to method calls

- Mock - An object with pre-defined answers to method calls, and has expectations of executions. Can throw an exception if an unexpected invocation is detected

- Spy - In Mockito Spies are Mock like wrappers around the actual object

#### Important Methods

- Verify 
    
    - Verify interactions can be used Mocked object was called
    
    - Used to verify number of times a mocked method has been called

- Argument Matcher 
    
    - Matches arguments passed to Mocked Method & will allow or disallow

- Argument Captor 
    
    - Captures arguments passed to a Mocked Method

    - Argument captors can be used to verify request data is properly being parsed and passed to service layer
    
    - Allows you to perform assertions of what was passed in to method

- Mock

    - Mock return values supply data back to controller
        - ie - object returned when getById is called on service

    - Mocks can also be instructed to throw exceptions to test exception handling



### Unit Testing with Mockito

- @Mock: Creates mock instances of dependencies.

- @InjectMocks: Creates an instance of the class under test and injects the mock dependencies into it.

- @ExtendWith(MockitoExtension.class): Enables the use of Mockito annotations within JUnit 5 tests by extending the JUnit 5 framework to support Mockito.

```java
@ExtendWith(MockitoExtension.class)
public class MyServiceTest {

    @Mock
    private MyRepository myRepository;

    @InjectMocks
    private MyService myService;

    @Test
    public void testGetEntityById() {
        
        // Arrange
        MyEntity entity = new MyEntity();
        
        // anyLong: is ArgumentMatcher
        when(myRepository.findById(anyLong())).thenReturn(Optional.of(entity));

        // Act
        MyEntity result = myService.getEntityById(1L);

        // Assert
        assertNotNull(result);

        // ArgumentCaptor example
        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(myRepository).findById(idCaptor.capture());
        assertEquals(1L, idCaptor.getValue());
    }
}
```

### Test Splices

- In Spring Boot, "test slices" are a set of specialized test configurations designed to test a specific part of the application in isolation, such as the web layer, data layer, or messaging layer. 

- They provide a focused environment to test only the relevant part of the application, minimizing the need to load the entire application context and reducing the test execution time.

- Here are the common test slices provided by Spring Boot:

#### Web Layer : @WebMvcTest

- Tests Spring MVC controllers.

- Loads only the web layer components (controllers, controller advice, filters, etc.).

- Example:

    ```java
    @WebMvcTest(MyController.class)
    public class MyControllerTests {
        @Autowired
        private MockMvc mockMvc;

        @Test
        public void testGet() throws Exception {
            mockMvc.perform(get("/endpoint"))
                .andExpect(status().isOk());
        }
    }
    ```

#### Data Layer : @DataJpaTest:

- Tests Spring Data JPA repositories.

- Configures an in-memory database, scans for @Entity classes, and configures Spring Data JPA repositories.

- Example:

    ```java
    @DataJpaTest
    public class MyRepositoryTests {
        @Autowired
        private MyRepository myRepository;

        @Test
        public void testFindAll() {
            List<MyEntity> entities = myRepository.findAll();
            assertThat(entities).isNotEmpty();
        }
    }
    ```

#### JDBC : @JdbcTest:

- Tests JDBC-related components.

- Configures an in-memory database and scans for JdbcTemplate and related components.

- Example:

    ```java
    @JdbcTest
    public class MyJdbcTests {
        @Autowired
        private JdbcTemplate jdbcTemplate;

        @Test
        public void testQuery() {
            List<MyEntity> entities = jdbcTemplate.query("SELECT * FROM my_table", new MyRowMapper());
            assertThat(entities).isNotEmpty();
        }
    }
    ```

#### JSON Testing : @JsonTest:

- Tests JSON serialization and deserialization.

- Configures ObjectMapper, JacksonTester, and other JSON-related components.

- Example:

    ```java
    @JsonTest
    public class MyJsonTests {
        @Autowired
        private JacksonTester<MyEntity> json;

        @Test
        public void testSerialize() throws Exception {
            MyEntity entity = new MyEntity(1, "name");
            assertThat(json.write(entity)).isEqualToJson("expected.json");
        }
    }
    ```

#### Messaging Layer : @MessagingTest

- Tests messaging components.

- Configures message-related beans and components.

- Example:

    ```java
    @MessagingTest
    public class MyMessagingTests {
        @Autowired
        private MyMessageListener listener;

        @Test
        public void testMessageListener() {
            Message<String> message = MessageBuilder.withPayload("test").build();
            listener.receiveMessage(message);
            // assertions
        }
    }
    ```

#### Service Layer : @RestClientTest:

- Tests REST clients (e.g., using RestTemplate or WebClient).

- Configures the necessary beans to test REST clients.

- Example:

    ```java
    @RestClientTest(MyRestClient.class)
    public class MyRestClientTests {
        @Autowired
        private MockRestServiceServer server;

        @Autowired
        private MyRestClient client;

        @Test
        public void testGet() {
            this.server.expect(requestTo("/endpoint"))
                    .andRespond(withSuccess("response", MediaType.APPLICATION_JSON));
            
            String response = client.get();
            assertThat(response).isEqualTo("response");
        }
    }
    ```

#### Configuration Slice : @ConfigurationPropertiesTest

- Tests configuration properties.

- Configures beans annotated with @ConfigurationProperties.

- Example:

    ```java
    @ConfigurationPropertiesTest
    public class MyConfigPropertiesTests {
        @Autowired
        private MyProperties properties;

        @Test
        public void testProperties() {
            assertThat(properties.getProperty()).isEqualTo("expectedValue");
        }
    }
    ```


### Test Splices for both unit and integration test

- We can combine the usage of test slices with integration tests to create a comprehensive test suite. 

- For example, you can use @WebMvcTest for controller-specific tests and @SpringBootTest for full-stack tests. 

- This approach helps to keep unit tests fast and focused while still ensuring end-to-end functionality through integration tests.

#### Why Use @AutoConfigureMockMvc?

    - Web Layer Testing:

        - MockMvc allows you to perform HTTP requests and assert the results, making it a powerful tool for testing your web layer.
        
        - You can test your controllers and their interactions with other beans without running a full server, which makes tests faster and more focused.
    
    - Integration with @SpringBootTest:

        - When you use @SpringBootTest, it loads the entire application context, which includes the web layer. 
        
        - Adding @AutoConfigureMockMvc configures MockMvc so you can perform requests against this context.
    
    - Comprehensive Testing:

    - With @AutoConfigureMockMvc, you can perform comprehensive tests that include Spring MVC infrastructure such as request mapping, exception handling, validation, etc.
    
    - When to Use @AutoConfigureMockMvc?
    
        - Full Integration Tests:

            - When you want to test the interaction between your controllers and the rest of the application, including services and repositories.
            
            - You use @SpringBootTest with @AutoConfigureMockMvc to get a full application context and test your web layer with MockMvc.
       
        - Avoiding Full Server Startup:

            - When you want to avoid starting an embedded servlet container but still want to test HTTP endpoints.

```java
// Full Integration Test
@SpringBootTest
@AutoConfigureMockMvc
public class MyIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testEndpoint() throws Exception {
        mockMvc.perform(get("/endpoint"))
               .andExpect(status().isOk());
    }
}
```


- Without @SpringBootTest we dont use SpringContext and use Mocked Beans for Unit Testing

- @MockBean: 
    - Useful in integration tests with Spring Boot to replace or add mock beans into the Spring context.

```java
// Controller Unit Test
@WebMvcTest(MyController.class)
public class MyControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean 
    private MyService service;

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get("/endpoint"))
               .andExpect(status().isOk());
    }
}
```

- JsonPath documentation : https://github.com/json-path/JsonPath
