package cinder

import bleak._
import bleak.swagger3.{Param, Produce, Query, api}
import com.fasterxml.jackson.databind.ObjectMapper

class CookiesRouter(objectMapper: ObjectMapper) extends BaseRouter {

  import CookiesRouter._

  val getCookie = get("/cookies") { ctx =>
    val cookies = ctx.request.cookies
    val res = for ((name, c) <- cookies) yield {
      name -> Option(c.value).getOrElse("")
    }
    objectMapper.writeValueAsString(CookieResponse(res.toMap))
  }

  val setCookie = get("/cookies/set") { ctx =>
    val body = newCookie(ctx)
    objectMapper.writeValueAsString(CookieResponse(body))
  }

  val deleteCookie = get("/cookies/delete") { ctx =>
    ctx.request.query.get("name") match {
      case Some(name) =>
        val nameArray = name.split(",")
        val cookies = ctx.request.cookies
        val res = for ((name, cookie) <- cookies if nameArray.contains(name)) yield {
          val newCookie = cookie.copy(maxAge = 0L, path = "/")
          ctx.response.cookies.add(newCookie)
          name -> cookie.value
        }
        objectMapper.writeValueAsString(CookieResponse(res.toMap))
      case None =>
        val requestCookies = ctx.request.cookies
        val res = for ((name, cookie) <- requestCookies) yield {
          val newCookie = cookie.copy(maxAge = 0L, path = "/")
          ctx.response.cookies.add(newCookie)
          name -> cookie.value
        }
        objectMapper.writeValueAsString(CookieResponse(res.toMap))
    }
  }

  private def newCookie(ctx: Context): Map[String, String] = {
    val it = ctx.request.query.iterator
    val res = for ((k, v) <- it) yield {
      val cookie = Cookie(name = k, value = v, path = "/", httpOnly = true)
      ctx.response.cookies.add(cookie)
      k -> v
    }
    res.toMap
  }

  api(getCookie)
    .operation("Returns cookie data.", tags = Seq("Cookies"))
    .responses(Produce[CookieResponse](mimeType = MimeType.Json))

  api(setCookie)
    .operation("Sets cookie(s) as provided by the query string", tags = Seq("Cookies"))
    .params(Param[String]("cookie1", in = Query), Param[String]("cookie2", in = Query))
    .responses(Produce[CookieResponse](mimeType = MimeType.Json))

  api(deleteCookie)
    .operation("Deletes cookie(s) as provided by the query string", tags = Seq("Cookies"))
    .params(Param[Array[String]]("name", in = Query))
    .responses(Produce[CookieResponse](mimeType = MimeType.Json))
}

object CookiesRouter {

  case class CookieResponse(cookies: Map[String, String])

}