package cinder

import bleak.{Meta, MimeType, Router}

class BaseRouter extends Router {
  override val produce: Meta.Produce = Meta.Produce(MimeType.Json)
}
