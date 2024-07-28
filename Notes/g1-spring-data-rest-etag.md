
### ETag, If-Match, and If-None-Match Headers

#### If-Match

- The ETag header provides a way to tag resources. 

- This can prevent clients from overriding each other while also making it possible to reduce unnecessary calls.

Consider the following example:

```java
// A POJO with a version number
class Sample {

	@Version Long version; 

	Sample(Long version) {
		this.version = version;
	}
}
```

- The POJO in the preceding example, when served up as a REST resource by Spring Data REST, has an ETag header with the value of the version field.

- We can conditionally PUT, PATCH, or DELETE that resource if we supply a If-Match header such as the following:

```sh
curl -v -X PATCH -H 'If-Match: <value of previous ETag>' ...
```

- Only if the resource’s current ETag state matches the If-Match header is the operation carried out. 

- This safeguard prevents clients from stomping on each other. Two different clients can fetch the resource and have an identical ETag. 

- If one client updates the resource, it gets a new ETag in the response. 

- But the first client still has the old header. 

- If that client attempts an update with the If-Match header, the update fails because they no longer match. 

- Instead, that client receives an HTTP 412 Precondition Failed message to be sent back. 

- The client can then catch up however is necessary.

#### If-None-Match header

- The If-None-Match header provides an alternative. Instead of conditional updates, If-None-Match allows conditional queries. 

Consider the following example:

```sh
curl -v -H 'If-None-Match: <value of previous etag>' ...
```

- The preceding command (by default) runs a GET. 

- Spring Data REST checks for If-None-Match headers while doing a GET. 

    - If the header matches the ETag, it concludes that nothing has changed and, instead of sending a copy of the resource, sends back an HTTP 304 Not Modified status code. 

    - If this supplied header value does not match the server-side version, send the whole resource. 