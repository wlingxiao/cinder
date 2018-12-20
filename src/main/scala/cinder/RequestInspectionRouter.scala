package cinder

import bleak.MimeType
import bleak.swagger3._
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import io.swagger.v3.oas.annotations.media.Schema

class RequestInspectionRouter(mapper: ObjectMapper with ScalaObjectMapper) extends BaseRouter {

  import RequestInspectionRouter._

  get("/headers") { ctx =>
    val headers = ctx.request.headers
    mapper.writeValueAsString(HeadersResponse(headers = headers.toMap))
  }

  get("/ip") { ctx =>
    val remoteHost = ctx.request.remoteHost
    s""" {"origin": "$remoteHost" } """
  }

  get("/user-agent") { ctx =>
    val ua = ctx.request.userAgent.getOrElse("")
    s""" { "user-agent": "$ua" } """
  }

  private val tags = Seq("Request inspection")

  api.doc("GET /headers")
    .operation("Return the incoming request's HTTP headers.", tags = tags)
    .params(Param[String](name = "header1", in = Header))
    .params(Param[String](name = "header2", in = Header))
    .responses(Produce[HeadersResponse](mimeType = MimeType.Json))

  api.doc("GET /ip")
    .operation("Returns the requester's IP Address.", tags = tags)
    .responses(Produce[IpResponse](mimeType = MimeType.Json))

  api.doc("GET /user-agent")
    .operation("Return the incoming requests's User-Agent header.", tags = tags)
    .params(Param[String](name = "user-agent", in = Header))
    .responses(Produce[UserAgentResponse](mimeType = MimeType.Json))
}

object RequestInspectionRouter {

  case class HeadersResponse(headers: Map[String, Any])

  case class UserAgentResponse(@Schema(name = "user-agent") userAgent: String)

  case class IpResponse(origin: String)


}