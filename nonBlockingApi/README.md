# HTTP Orchestration  API - Async Refactoring Assignment

## ✅ Overview
This project implements a **refactored asynchronous HTTP invocation API** using **
Java 11's HttpClient** and **CompletableFuture** to improve scalability and responsiveness. 
The system supports dynamic HTTP method invocations (GET, POST, PUT, DELETE) and 
is designed for high-concurrency usage, supporting multiple parallel requests.

---

## 🏗️ Architecture

### Key Components:

1. **NonBlockingApiController.java**:
    - Acts as the entry point for incoming HTTP requests.
    - Accepts POST requests with JSON payloads, which specify the HTTP method, URL, headers, and body of the request.
    - The controller invokes the `RestFactoryAsync` service asynchronously and returns a response immediately (non-blocking).

2. **RestFactoryAsync.java**:
    - Handles the actual HTTP invocations using Java 11's `HttpClient`.
    - Each request is executed asynchronously via `CompletableFuture`, allowing for non-blocking I/O operations.
    - Supports various HTTP methods (GET, POST, PUT, DELETE), SSL configuration, and custom timeouts.
    - The use of `CompletableFuture` ensures the main thread is not blocked while waiting for HTTP responses,
    - improving throughput and responsiveness.

3. **InputRequest.java**:
    - A simple model class representing the incoming JSON request body, with fields for HTTP method, URL, headers, 
    - and body content.

---

## 🔧 Tech Stack

### Core Technologies:
- **Java 11+**: The primary language for backend logic. Java 11 introduced enhanced features like the `HttpClient` API for
- making asynchronous HTTP requests.
- **Spring Boot  **: A lightweight framework used for building RESTful services and handling the controller layer.
- **k6**: A modern load testing tool to simulate large-scale HTTP requests and measure performance.
- **Apache HttpClient (for synchronous version)**: Used initially for the synchronous implementation 
- before refactoring to the Java 11 `HttpClient`.

### Why These Choices?
- **Java 11's HttpClient**: It natively supports asynchronous I/O operations with 
- `CompletableFuture`, allowing for better scalability and responsiveness compared to older libraries like Apache HttpClient.
- **CompletableFuture**: Enables non-blocking, asynchronous handling of HTTP requests, which significantly improves
- throughput in high-concurrency scenarios.
- **k6**: Chosen for load testing due to its simplicity, ease of use, and scalability for 
- performance testing under various loads.

---

## ⚖️ Trade-offs & Limitations

### ✅ Pros:
1. **Improved Scalability**:
    - By refactoring to a non-blocking, asynchronous model, the API can handle many more concurrent requests 
    - without blocking threads, making it highly scalable.

2. **Faster Response Time**:
    - Async processing minimizes response times by not waiting for HTTP responses before handling other requests.

3. **Modern and Efficient**:
    - Java 11's native `HttpClient` and `CompletableFuture` provide a modern, efficient approach to building 
    - high-performance APIs.

4. **Reduced Thread Usage**:
    - Async calls allow for fewer threads to be used, thus reducing overhead on the system.

---

### ⚠️ Cons:
1. **Increased Complexity**:
    - Asynchronous programming requires careful handling of errors and more complex code, 
    - which might be harder to debug, especially when handling exceptions in `CompletableFuture`.

2. **Error Handling**:
    - With asynchronous tasks, errors can propagate through the `CompletableFuture` chain, making it more
    - challenging to manage exceptions compared to synchronous operations.

3. **Thread Management**
   - Asynchronous calls using `CompletableFuture` reduce thread 
   - blocking by leveraging the default common thread pool (`ForkJoinPool.commonPool()`).
   - While this default behavior works well for moderate loads, thread usage can also 
   - be managed manually by configuring a custom `ExecutorService`.
   - Using a custom executor gives more control over thread allocation and can help optimize 
   - performance, especially under high concurrency or load test scenarios.


4. **Learning Curve**:
    - Developers need a strong understanding of async programming concepts and Java's `CompletableFuture` 
    - API to properly work with this implementation.

5. **Latency in Some Cases**:
    - Though async provides great throughput, it may introduce latency in certain cases, 
    - especially if there’s a dependency chain of async operations that must complete before a 
    - final result is returned.


##  Contact

If you have any questions or need further information, feel free to reach out to me:

**Name**: Pradnya Panaskar  
**Email**: pradnya.panaskar99@gmail.com
