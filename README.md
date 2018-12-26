Cinder
============

## Simple HTTP utils.
Powered by [Bleak](https://github.com/wlingxiao/bleak) and inspired by [httpbin](https://github.com/requests/httpbin)
#### Build Setup

```bash
# clone bleak repository and build
git clone https://github.com/wlingxiao/bleak.git -b dev
cd bleak
sbt clean publishLocal

# clone this repository and build
git clone https://github.com/wlingxiao/cinder.git
cd cinder
sbt clean assembly

```

Run local at `http://127.0.0.1:7865` 
```shell
java -jar ./target/scala-2.12/cinder-assembly-0.0.1-SNAPSHOT.jar run
```
Visit `http://127.0.0.1:7865/swagger-ui.html`


Deploy to remote server
```scala
object CinderApp extends Cli {

  override def servers: Seq[Server] = {
    // put your server info
  }
}
```

Run deploy command
```shell
java -jar ./target/scala-2.12/cinder-assembly-0.0.1-SNAPSHOT.jar depoly
```