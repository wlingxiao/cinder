package cinder

import bleak.Status
import bleak.Status._
import bleak.netty.{EmbeddedClient, Netty}

class StatusCodeRouterTests extends BaseTest {

  private var app: Netty = _
  private var client: EmbeddedClient = _
  private var statusCodeRouter: StatusCodeRouter = _
  before {
    app = new Netty
    statusCodeRouter = new StatusCodeRouter
    app.use(statusCodeRouter)
    client = new EmbeddedClient(app)
  }

  test("return status code") {
    for (m <- statusCodeRouter.methods) {
      val res = client.fetch(m, "/status/200")
      assert(res.status == Status.Ok)

      val res1 = client.fetch(m, "/status/400")
      assert(res1.status == Status.BadRequest)
    }
  }

  test("return random status code") {
    for (m <- statusCodeRouter.methods) {
      val res = client.fetch(m, "/status/200,300,400,500")

      val codes = Seq(Ok, MultipleChoices, BadRequest, InternalServerError)
      assert(codes.contains(res.status))
    }
  }

}
