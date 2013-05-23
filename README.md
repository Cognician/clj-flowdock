[![Build Status](https://travis-ci.org/RallySoftware/clj-flowdock.png?branch=master)](https://travis-ci.org/RallySoftware/clj-flowdock)
# clj-flowdock
Clojure Library for interacting with flowdock
The api is pretty straight forward and implements most of the public facing end points that flowdock uses.

# Leiningen
https://clojars.org/com.rallydev/clj-flowdock
```clojure
[com.rallydev/clj-flowdock "0.2.0"]
```

# Maven
```xml
<dependency>
  <groupId>com.rallydev</groupId>
  <artifactId>clj-flowdock</artifactId>
  <version>0.2.0</version>
</dependency>
```

# Environment Variables
```bash
export FLOWDOCK_TOKEN=<user flowdock token>
```

## API

### Organizations
```clojure
(clj-flowdock.api.organization/list)
```
Returns all organizations that the flowdock user belongs to.

```clojure
(clj-flowdock.api.organization/id)
```
Returns the org-id of the first org the user belongs to (I am unaware if a user can belong to multiple orgs). The org id
is used when building a flow-id.

```clojure
(clj-flowdock.api.organization/get-users)
```
Returns all users for the first org the user belongs to.

### Users
```clojure
(clj-flowdock.api.user/find [key value])
```
This function will find a user (using clj-flowdock.api.organization/get-users) where the values are equal.

```clojure
(clj-flowdock.api.user/get [id]
```
Returns a user from their id.

### Flows
```clojure
(clj-flowdock.api.flow/list)
```
Returns all flows where the joined attribute is set to true.

```clojure
(clj-flowdock.api.flow/list-all)
```
Returns all flows where organization is the access_mode and even includes flows where joined is set to false.

```clojure
(clj-flowdock.api.flow/find [key value])
```
Return all flows where the values are equal for a given key.

```clojure
(clj-flowdock.api.flow/get [id])
```
Returns a flow for a given id. Flow ids are given in the format -- org-id/flow-id.

```clojure
(clj-flowdock.api.flow/add-user [flow-id user-id])
```
This sets the joined flag for a user on a specific flow to true.

```clojure
(clj-flowdock.api.flow/block-user [flow-id user-id])
```
This sets the disabled flag to true for a user on a given flow. This blocks all access to a flow, you need to reinvite the user
to unblock them. This is not a mechanism for leaving a flow.

```clojure
(clj-flowdock.api.flow/create [org-id name])
```
Allows you to create a flow in a given org with the specified name.

```clojure
(clj-flowdock.api.flow/update [flow-id attributes])
```
Update the attributes (specified as a map) for the given flow.

## Dependencies
```clojure
[org.clojure/clojure "1.5.0"]
[com.rallydev/clj-flowdock "0.2.1"]
[com.rallydev/clj-jenkins "0.1.5"]
[org.codehaus.groovy/groovy-all "2.1.0"]
[clj-http "0.7.2"]
[org.clojure/java.classpath "0.2.0"]
[fs "1.3.2"]
[org.clojure/tools.logging "0.2.6"]
[ch.qos.logback/logback-classic "1.0.9"]
[compojure "1.1.3"]
[netty-ring-adapter "0.2.4"]
[tentacles "0.2.4"]
```

# License
Copyright (c) Rally Software Development Corp. 2013  
Distributed under the MIT License.

