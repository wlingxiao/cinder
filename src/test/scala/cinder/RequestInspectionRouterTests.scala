package cinder

import bleak.netty.{EmbeddedClient, Netty}

class RequestInspectionRouterTests extends BaseTest {

  private var app: Netty = _
  private var client: EmbeddedClient = _
  before {
    app = new Netty
    app.use(new RequestInspectionRouter(objectMapper))
    client = new EmbeddedClient(app)
  }


  test("return request's headers") {
    val res = client.get("/headers", headers = Map("header1" -> "value1", "header2" -> "value2"))

    val ret = objectMapper.readValue[Map[String, Map[String, String]]](res.body.string)

    assert(ret("headers")("header1") == "value1")
    assert(ret("headers")("header2") == "value2")
  }

}
