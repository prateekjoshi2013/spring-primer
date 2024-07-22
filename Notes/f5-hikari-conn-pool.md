### Hikari Connection Pool

- A opening and maintaining database connection consumes a lot of resources.To avoid consuming resources and time a connection pool of open connections is maintained and is recycled.

- HikariCP (Hikari Connection Pool) is a fast, reliable, and lightweight JDBC connection pool. 

- It is designed to provide a high-performance connection pooling mechanism for managing database connections in Java applications. 

### Key Features of HikariCP

- High Performance: 
    
    - HikariCP is known for its high performance and low latency. 
   
    - It is often benchmarked as one of the fastest connection pools available.

- Lightweight: 
    
    - It has a small footprint, both in terms of library size and runtime overhead.

- Reliability: 
    
    - It is designed to be robust and handle various failure scenarios gracefully, ensuring stable database connections.

- Ease of Use: 
    
    - HikariCP is straightforward to configure and integrates well with various frameworks and libraries, such as Spring Boot.

- Customization: 

    - It offers a range of configuration options to fine-tune the behavior of the connection pool according to application requirements.

### Uses of HikariCP

- Connection Management: 

    - HikariCP efficiently manages a pool of database connections, reducing the overhead of establishing new connections repeatedly. 
    
    - This is crucial for applications with high database interaction.

- Resource Optimization: 

    - By reusing connections from the pool, HikariCP helps optimize resource utilization, reducing the load on the database server and improving overall application performance.

- Scalability: 
    
    - HikariCP can handle a large number of concurrent database connections, making it suitable for applications with high scalability requirements.

- Improved Performance: 
    
    - With its low overhead and fast connection acquisition and release, HikariCP enhances the performance of database-intensive applications.

- Integration with Frameworks: 
    
    - HikariCP is commonly used with frameworks like Spring Boot, which provides seamless integration and configuration through application properties.

