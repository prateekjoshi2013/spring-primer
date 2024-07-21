Taken from : https://thorben-janssen.com/generate-uuids-primary-keys-hibernate/

### UUID vs Numerical Id
- The main advantage of a UUID is its (practical) global uniqueness which provides a huge advantage for distributed systems.

- If you use the typical numerical ID that gets incremented for each new record, you need to generate all IDs by the same system component. 

    - In most cases, this is a sequence for each table that’s managed by your database. 

    - This makes that sequence the single source of failure. Other approaches, e.g., a clustered database or any other horizontally scaled number generator, require communication between the nodes. 

    - That obviously creates some effort that slows down the generation of your primary key values.


- You don’t need any of this when using a globally unique UUID. 

- Each component can generate its own UUIDs, and there will not be any conflicts. 

- That’s why UUIDs have become popular in microservice-based architectures or when developing offline clients.

### Disavantage 

- the UUID also has some disadvantages. 

- It’s 4 times larger than a numerical ID and can’t be handled as efficiently. 


- you can persist UUIDs with Hibernate. 

- When doing that, you need to decide how you want to generate the UUID value. 
    
    - We can, of course, generate it yourself and set it on your entity object before persisting it. 
    
    - Or, if you’re using Hibernate 4, 5, or 6 or JPA 3.1, you can define a generation strategy in your entity mappings.

### UUID Strategy supported by Hibernate 4, 5 and 6

- As mentioned earlier, IETF RFC 4122 defines 4 different strategies to generate UUIDs. 

- Hibernate supports 2 of them:

    - Default strategy generates the UUID based on random numbers (IETF RFC 4122 Version 4).

    - Generator that uses the IP address of the machine and a timestamp (IETF RFC 4122 Version 1).

### Random Number based UUID generation

#### Random number based UUID in Hibernate 6

    - Using Hibernate 6, you can annotate your primary key attribute with @UuidGenerator and set the style to RANDOM, AUTO, or don’t specify it. 

    - In all 3 cases, Hibernate will apply its default strategy.

    ```java
        @Entity
        public class Book {
            @Id
            @GeneratedValue
            @UuidGenerator
            private UUID id;
        }
    ```
#### Random number based UUID in Hibernate 4 and 5

    - You need to annotate your primary key attribute with a @GeneratedValue annotation. 

    - In that annotation, you need to reference a custom generator and define that generator using Hibernate’s @GenericGenerator annotation. 

    - The @GenericGenerator annotation requires 2 parameters, the name of the generator and the name of the class that implements the generator. 

    - In this case, I called the generator “UUID” and Hibernate shall use the class org.hibernate.id.UUIDGenerator.


    ```java
        @Entity
        public class Book {
        
            @Id
            @GeneratedValue(generator = "UUID")
            @GenericGenerator(
                name = "UUID",
                strategy = "org.hibernate.id.UUIDGenerator"
            )
            private UUID id;
        }
    ```

### IP and timestamp based UUID 

- Hibernate uses the IP address and timestamp to generate uuid. In general, this is not an issue. But if the servers of your distributed system are running on different networks, you should make sure that none of them share the same IP address.

#### IP and timestamp based UUID in Hibernate 6

- The @UuidGenerator annotation introduced in Hibernate 6 has a style attribute that you can use to define how Hibernate shall generate the UUID value.

- When you set it to TIME, it uses a timestamp and the IP address to generate the UUID value.

    ```java
        @Entity
        public class Book {
            
            @Id
            @GeneratedValue
            @UuidGenerator(style = Style.TIME)
            private UUID id;
        }
    ```

#### IP and timestamp based UUID in Hibernate 4 and 5

- If you’re using Hibernate 4 or 5, you need to set an additional parameter on the @GenericGenerator annotation to define the generation strategy.

- You define the strategy by providing a @Parameter annotation with the name uuid_gen_strategy_class and the fully qualified class name of the generation strategy as the value.

    ```java
        @Entity
        public class Book {
        
            @Id
            @GeneratedValue(generator = "UUID")
            @GenericGenerator(
                name = "UUID",
                strategy = "org.hibernate.id.UUIDGenerator",
                parameters = {
                    @Parameter(
                        name = "uuid_gen_strategy_class",
                        value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
                }
            )
            @Column(name = "id", updatable = false, nullable = false)
            private UUID id;
        }
    ```