# tcp-server
tcp-server

This is a sample tcp-server


You must have the following software installed:
1. java 8 - http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
2. maven - https://maven.apache.org/install.html
3. mongodb -
In order to use persistence you need to have a mongodb installed on your local machine with the following configurations:
database name: testserver
user: user
password: password
port: 27017 (default)

Here is the full shell command:
use testserver
db.createUser(
   {
     user: "user",
     pwd: "password",
     roles: [ "readWrite", "dbAdmin" ]
   }
)


It’s API includes:
• getAllKeys(String pattern) – Returns all keys matching pattern - the pattern should be started with ^ and ended with $.
• rightAdd(String K, String V) – adds a value to key K, from the right
• leftAdd(String K, String V) – adds a value to key K, from the left
• set(String K, List<String> V)
• get(String K) – gets a list by its key

For example (write in client console):
set a [1,2,3] -> void
get a -> [1,2,3]
set b [1]
rightAdd b 2
get b -> [1,2]
set alex [1,2]
get alex -> [1,2]
getAllKeys ^a(.)*$ -> [a, alex]
set a []
getAllKeys ^a(.)*$ -> [alex]

In order to build the project (here the example is for tcp-server, but the same applies for the tcpclient):
1. Go to main folder 
2. run 'mvn clean install' in terminal
3. Run jar in /target java -jar tcp.server-1.0-SNAPSHOT.jar

How did I create the server?
For the server I used java.net.ServerSocket (an implementation of Socket interface) and then I listen to connections from client and then
send asynchronously to one of its handlers. The server can handle simultaneously up to 2 clients (this was made for testing and can be easily
modified). I used Spring boot with mongodb started to ease the dependecy management and the object creation.

Difficulties:
1. Design - was the hardest part and actually took more than half of the construction time. It took me some time to decide whether I'm going
to have 1 or 2 caches (one for key and the other for pattern). Eventually, I used only one.
2. Clean code -  about 1/4 of the time was dedicated to it. Tried to write it as clean as possible.
3. All these corner cases with " " and [1,2,3] vs [1, 2, 3] parsing.
