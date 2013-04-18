import org.specs._

object LiftJsonSpec extends Specification {
  import dispatch.classic._
  import dispatch.liftjson.Js._
  import org.json4s.{JString, JField, JArray, JObject}

  val test = :/("technically.us") / "test.json"
  
  "Json Parsing" should {
    "find title of test glossary" in {
      val http = new Http
      http(test ># { js =>
        for (JString(s) <- js \ "glossary" \ "title") yield s
      } ) must_== List("example glossary")
    }
    "find reference list" in {
      val http = new Http
      http(test ># { js =>
        ((js\"glossary"\"GlossDiv"\"GlossList"\"GlossEntry"\"GlossDef"\"GlossSeeAlso") match {case JArray(arr) => arr}) map {case JString(s) => s}
      } ) must_== List("GML","XML")
    }
  }
}
