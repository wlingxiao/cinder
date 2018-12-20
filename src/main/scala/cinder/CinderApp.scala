package cinder

import bleak._
import bleak.netty.Netty
import bleak.swagger3._

object CinderApp {

  def main(args: Array[String]): Unit = {
    val app = new Goa with Netty
    app.use(new SwaggerUIRouter)
    app.use(new ApiDocsRouter(apiConfig))
    app.use(new HttpMethodRouter(Json.objectMapper))
    app.use(new StatusCodeRouter)
    app.use(new RequestInspectionRouter(Json.objectMapper))
    app.run()
  }

  private val apiConfig: Config = {
    Config(
      info = Info(
        title = "localhost",
        desc = "A simple HTTP Request & Response Service.",
        version = "0.0.1"),
      tags = Seq(
        Tag(name = "HTTP Methods", desc = "Testing different HTTP verbs"),
        Tag(name = "Status Code", desc = "Generates responses with given status code"),
        Tag(name = "Request inspection", desc = "Inspect the request data")
      )
    )
  }

}
