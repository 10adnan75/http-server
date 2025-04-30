
# 🧠 Build Your Own HTTP Server (Java)

[![progress-banner](https://backend.codecrafters.io/progress/http-server/0608a018-a0c9-441a-8db0-2ff432858d58)](https://app.codecrafters.io/users/codecrafters-bot?r=2qF)

This repository contains my Java implementation of a fully functional HTTP/1.1 server, built as part of the [Codecrafters "Build Your Own HTTP Server"](https://app.codecrafters.io/courses/http-server/overview) challenge.

---

## 📦 Features Implemented

- ✅ HTTP/1.1 protocol parsing (requests and responses)
- ✅ Persistent TCP connections with `Connection: keep-alive` and `Connection: close`
- ✅ `/echo/<msg>` endpoint (returns plain text or gzip-compressed based on headers)
- ✅ `/user-agent` endpoint (returns client's User-Agent)
- ✅ `/files/<filename>` (supports GET and POST for file retrieval and storage)
- ✅ Custom headers, status codes, and MIME types
- ✅ Multithreaded request handling using a thread pool

---

## 🛠️ How to Run

### Prerequisites
- Java 11+
- Maven

### Run the server
```bash
./your_program.sh
```

### Or using Maven directly:
```bash
mvn clean compile exec:java -Dexec.mainClass="Main"
```

---

## 🧪 Test it manually (examples)

```bash
curl -v http://localhost:4221/
curl -v http://localhost:4221/user-agent
curl -v http://localhost:4221/echo/hello
curl -v --header "Accept-Encoding: gzip" http://localhost:4221/echo/compressed
curl -X POST --data "This is a file." http://localhost:4221/files/test.txt
curl http://localhost:4221/files/test.txt
```

---

## 📁 Project Structure

- `HTTPServer.java` – Core server logic (multi-threaded socket handling, routing)
- `HTTPRequest.java` – Request parsing (method, path, headers, body)
- `HTTPResponse.java` – Response builder with status, headers, and body
- `ContentType.java` – MIME type handling
- `Main.java` – Entry point

---

## 🎯 Why This Project?

This project was a deep dive into how real web servers work under the hood. It helped me learn:

- Low-level networking with Java `Socket` APIs
- HTTP request parsing and protocol rules
- How headers, compression, and status codes work
- Graceful connection handling and concurrency

---

## 📚 References

- [HTTP/1.1 RFC 2616](https://www.w3.org/Protocols/rfc2616/rfc2616.html)
- [Codecrafters.io - HTTP Server Challenge](https://app.codecrafters.io/courses/http-server)
