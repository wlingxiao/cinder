package cinder

import bleak._
import bleak.cli.{Cli, Server}
import bleak.netty.Netty
import bleak.swagger3._

object CinderApp extends Cli {
  def app: Application = {
    val app = new Netty {
    }
    app.use(new SwaggerUIRouter)
    app.use(new ApiDocsRouter(apiConfig))
    app.use(new HttpMethodRouter(Json.objectMapper))
    app.use(new StatusCodeRouter)
    app.use(new RequestInspectionRouter(Json.objectMapper))
    app.use(new DynamicDataRouter(Json.objectMapper))
    app.use(new CookiesRouter(Json.objectMapper))
  }

  override def servers: Seq[Server] = {
    val users = System.getenv("CINDER_USERS").split(",")
    val hosts = System.getenv("CINDER_HOSTS").split(",")
    val remotePaths = System.getenv("CINDER_REMOTE_PATHS").split(",")

    (0 until users.length) map { i =>
      Server(user = users(i).trim, host = hosts(i).trim, remotePath = remotePaths(i).trim)
    }
  }

  private val apiConfig: Config = {
    Config(
      info = Info(
        title = "Cinder",
        desc = "A simple HTTP Request & Response Service.",
        version = "0.0.1"),
      tags = Seq(
        Tag(name = "HTTP Methods", desc = "Testing different HTTP verbs."),
        Tag(name = "Status Code", desc = "Generates responses with given status code."),
        Tag(name = "Request inspection", desc = "Inspect the request data."),
        Tag(name = "Dynamic data", desc = "Generates random and dynamic data."),
        Tag(name = "Cookies", desc = "Creates, reads and deletes Cookies")
      )
    )
  }

  def main(args: Array[String]): Unit = {
    Cli.run(this, args)
  }

}
