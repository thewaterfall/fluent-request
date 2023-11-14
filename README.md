# Fluent Request Builder

Fluent Request Builder is a powerful and flexible Java library for constructing HTTP requests in a fluent and expressive manner. It simplifies the process of creating and sending HTTP requests by providing a chainable API for building requests with ease.

## Features

- Clean, readable and fluent syntax.
- Customizable request parameters and headers.
- Support various HTTP methods, including GET, POST, PUT, DELETE, and more.
- Support for JSON, multipart, form data, and other content types.
- [OkHttp](https://github.com/square/okhttp) library as the base HTTP client
- [Jackson](https://github.com/FasterXML/jackson) library as JSON parser to return expected response

## Installation
Fluent Request Builder can be easily installed using JitPack, see Gradle and Maven examples below.

### Gradle
Add the following to your build.gradle file:

```
repositories {
    mavenCentral()
    maven { url "https://jitpack.io" }
}

dependencies {
    implementation 'com.github.thewaterfall:fluent-request-builder:1.0.0'
}
```

### Maven
Add the following to your pom.xml file:

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.thewaterfall</groupId>
        <artifactId>fluent-request</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

## Usage

The `FluentRequest` class is the core of the library, providing a fluent interface for constructing and sending HTTP requests. It supports various HTTP methods, request body types, headers, and authentication methods. Additionally, it allows customization of the OkHttp client and Jackson ObjectMapper.

### Example 1: Fetching an entity

Let's assume you want to fetch a single article by its ID. The endpoint for this example is `https://example.com/articles/1`.

```
FluentRequest.request("https://example.com/articles/1", Article.class)
    .get();
```

### Example 2: Creating an entity
Suppose you want to create a new article by sending a POST request with the article data. Let's assume the endpoint is https://example.com/articles.

```
Article newArticle = new Article("New Article Title", "Content goes here");

FluentRequest.request("https://example.com/articles", Article.class)
    .bearer("YOUR_ACCESS_TOKEN") // Assuming you need authentication
    .body(newArticle)
    .post();
```

### Example 3: Updating an entity
Let's say you want to update an existing article with ID 1. The endpoint for this example is https://example.com/articles/1.

```
Article updatedArticle = new Article("Updated Title", "Updated content");

FluentRequest.request("https://example.com/articles/1", Article.class)
    .bearer("YOUR_ACCESS_TOKEN")
    .body(updatedArticle)
    .patch();
```

### Example 4: Uploading a file

```
File file = new File("path/to/file.txt");

FluentRequest.request("https://example.com/upload", String.class)
    .multipart()
        .add("username", "john_doe")  // Additional form field
        .add("fileKey", "filename.txt", fileToUpload)  // File to upload
    .build()
    .post();
```

### Example 5: Using variables and parameters
The below example will result in building the following URL https://example.com/articles/1/comments?sort=asc.

```
FluentRequest.request("https://example.com/articles/{articleId}/comments", Comment[].class)
    .variable("articleId", 1)
    .parameter("sort", "asc")
    .get();
```
