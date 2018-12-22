package cinder

import bleak._
import bleak.Method._
import bleak.swagger3._

import scala.util.Random

class StatusCodeRouter extends Router {

  val methods = Seq(Get, Post, Put, Delete, Patch)

  route("/status/{codes}", methods = methods) { ctx =>
    ctx.request.paths.get("codes") match {
      case Some(codes) =>
        val codeArray = codes.split(",")
        if (codeArray.length == 1) {
          statusCode(codeArray.head)
        } else {
          val pos = Random.nextInt(codeArray.size)
          statusCode(codeArray(pos))
        }
      case None => NotFound()
    }
  }

  private def statusCode(code: String): Result = {
    try {
      Status(s = Status(code.toInt))
    } catch {
      case _: NumberFormatException =>
        Status(s = Status.BadRequest)
    }
  }

  for (m <- methods) {
    api.doc(s"${m.name} /status/{codes}")
      .operation("Return status code or random status code if more than one are given", tags = Seq("Status Code"))
      .params(Param[Array[String]](in = Path, name = "codes"))
      .responses(
        Produce(name = "100", desc = "Informational responses"),
        Produce(),
        Produce(name = "300", desc = "Redirection"),
        Produce(name = "400", desc = "Client Errors"),
        Produce(name = "500", desc = "Server Errors"))
  }
}
