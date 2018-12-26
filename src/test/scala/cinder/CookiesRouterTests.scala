package cinder

import bleak.Cookie
import bleak.netty.{EmbeddedClient, Netty}

class CookiesRouterTests extends BaseTest {

  import CookiesRouter._

  private var app: Netty = _
  private var client: EmbeddedClient = _
  before {
    app = new Netty
    app.use(new CookiesRouter(objectMapper))
    client = new EmbeddedClient(app)
  }

  test("get cookies") {
    val res = client.get("/cookies", cookies = Seq(Cookie("cookie1", "value1"), Cookie("cookie2", "value2")))

    val cookieResponse: CookieResponse = objectMapper.readValue[CookieResponse](res.body.string)
    val cookies = cookieResponse.cookies

    cookies.get("cookie1") shouldEqual Some("value1")
    cookies.get("cookie2") shouldEqual Some("value2")
  }

  test("set cookies") {
    val res = client.get("/cookies/set", params = Map("cookie1" -> "value1", "cookie2" -> "value2"))
    val cookieResponse = objectMapper.readValue[CookieResponse](res.body.string)
    val cookies = cookieResponse.cookies

    res.cookies("cookie1").value shouldEqual "value1"
    res.cookies("cookie2").value shouldEqual "value2"

    cookies.get("cookie1") shouldEqual Some("value1")
    cookies.get("cookie2") shouldEqual Some("value2")
  }

  test("delete cookies") {
    val res = client.get("/cookies/delete", cookies = Seq(Cookie("cookie1", "value1"), Cookie("cookie2", "value2")))

    val cookie1 = res.cookies("cookie1")
    cookie1.value shouldEqual "value1"
    cookie1.maxAge shouldEqual 0L
    cookie1.path shouldEqual "/"

    val cookie2 = res.cookies("cookie2")
    cookie2.value shouldEqual "value2"
    cookie2.maxAge shouldEqual 0L
    cookie2.path shouldEqual "/"

    val cookieResponse: CookieResponse = objectMapper.readValue[CookieResponse](res.body.string)
    val cookies = cookieResponse.cookies
    cookies.get("cookie1") shouldEqual Some("value1")
    cookies.get("cookie2") shouldEqual Some("value2")
  }

}
