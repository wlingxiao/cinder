package cinder

import java.util.concurrent.{Executors, TimeUnit}

import bleak._
import bleak.swagger3.{Param, _}
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import io.swagger.v3.oas.annotations.media.Schema

import scala.collection.mutable
import scala.concurrent.Promise


class HttpMethodRouter(mapper: ObjectMapper with ScalaObjectMapper) extends BaseRouter {

  private val pool = Executors.newScheduledThreadPool(1)

  import HttpMethodRouter._

  get("/get")(ctx => requestData(ctx))

  post("/post")(ctx => requestData(ctx))

  put("/put") { ctx =>

  }

  delete("/delete") { ctx =>

  }

  get("/delay/{seconds}") { ctx =>
    ctx.request.paths.get("seconds") match {
      case Some(seconds) =>
        try {
          val time = seconds.toInt
          if (time >= 0 && time <= 10) {
            val promise = Promise[Result]()
            pool.schedule(new Runnable {
              override def run(): Unit = {
                promise.success(Ok(requestData(ctx)))
              }
            }, seconds.toInt, TimeUnit.SECONDS)
            promise.future
          } else Status(s = Status.BadRequest)
        } catch {
          case _: NumberFormatException =>
            Status(s = Status.BadRequest)
        }
      case None => NotFound()
    }
  }

  private def requestData(ctx: Context): String = {
    val request = ctx.request
    val headers = request.headers
    val query = request.query
    val formParams = request.form

    val args = query.iterator.map { entry =>
      entry._1 -> query.getAll(entry._1)
    }
    val res = new mutable.HashMap[String, Any]()
    if (hasBody(request)) {
      val form = formParams.iterator.map { entry =>
        entry._1 -> formParams.getAll(entry._1)
      }
      res.put("form", form.toMap)
    }
    res.put("headers", headers)
    res.put("origin", request.remoteHost)
    res.put("args", args.toMap)

    mapper.writeValueAsString(res)
  }

  private def hasBody(request: Request): Boolean = {
    request.method != Method.Get && request.method != Method.Head
  }

  api.doc("GET /get")
    .operation("The request's query parameters", tags = Seq("HTTP Methods"))
    .params(
      Param[String](name = "arg1", in = Query, desc = "arg1"),
      Param[String](name = "arg2", in = Query, desc = "arg2"),
      Param[Array[String]](name = "arg3", in = Query, desc = "arg3"))
    .responses(Produce[GetResponseBody](desc = "The request’s query parameters.", mimeType = MimeType.Json))

  api.doc("GET /delay/{seconds}")
    .operation("Returns a delayed response (max of 10 seconds).", tags = Seq("Dynamic data"))
    .params(
      Param[Int](name = "seconds", in = Path),
      Param[String](name = "arg1", in = Query, desc = "arg1"),
      Param[String](name = "arg2", in = Query, desc = "arg2"),
      Param[Array[String]](name = "arg3", in = Query, desc = "arg3"))
    .responses(Produce[GetResponseBody](desc = "The request’s query parameters.", mimeType = MimeType.Json))

  api.doc("POST /post")
    .operation("The request's POST parameters.", tags = Seq("HTTP Methods"))
    .params(
      Param[String](name = "arg1", in = Query, desc = "arg1"),
      Param[Array[String]](name = "arg2", in = Query, desc = "arg2"),
      Param[String](name = "header1", in = Header, desc = "header1"),
      Param[Array[String]](name = "header2", in = Header, desc = "header2"))
    .requestBody(consumes = Seq(
      Consume[PostConsumeForm](mimeType = MimeType.WwwForm),
      Consume[Map[String, Any]](mimeType = MimeType.Json)))
    .responses(Produce[PostResponseBody](desc = "The request’s POST parameters.", mimeType = MimeType.Json))
}

object HttpMethodRouter {

  case class GetResponseBody(args: Map[String, Any],
                             headers: Map[String, Any],
                             origin: String,
                             url: String)

  case class PostResponseBody(args: Map[String, Any],
                              form: Map[String, Any],
                              headers: Map[String, Any],
                              origin: String,
                              url: String)

  case class PostConsumeForm(@Schema(description = "arg1") arg1: String,
                             @Schema(description = "arg2") arg2: Array[String])

  case class PostFormRequestBody(form: Map[String, Any])

}