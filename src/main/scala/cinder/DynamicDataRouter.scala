package cinder

import java.util.{Base64, UUID}

import bleak._
import bleak.swagger3._
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

class DynamicDataRouter(mapper: ObjectMapper with ScalaObjectMapper) extends BaseRouter {

  import DynamicDataRouter._

  get("/base64/{value}") { ctx =>
    ctx.request.paths.get("value") match {
      case Some(value) =>
        try {
          mapper.writeValueAsString(Base64Response(new String(Base64.getDecoder.decode(value))))
        } catch {
          case _: IllegalArgumentException =>
            Status(s = Status.BadRequest)
        }
      case None => NotFound()
    }
  }

  get("/uuid") {
    mapper.writeValueAsString(UuidResponse(UUID.randomUUID().toString))
  }

  api.doc("GET /base64/{value}")
    .operation("Decodes base64 url-encoded string.", tags = Seq("Dynamic data"))
    .params(Param[String](name = "value", in = Path))
    .responses(Produce[Base64Response](mimeType = MimeType.Json))

  api.doc("GET /uuid")
    .operation("Return a UUID4.", tags = Seq("Dynamic data"))
    .responses(Produce[UuidResponse](mimeType = MimeType.Json))
}

object DynamicDataRouter {

  case class Base64Response(res: String)

  case class UuidResponse(uuid: String)

}