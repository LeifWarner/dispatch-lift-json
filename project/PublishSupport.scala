import sbt._
import Keys._
import Defaults._
// parse the maven settings.xml
import scala.xml.XML
// For embedding git version info

object PublishSupport extends Build {


  lazy val dispatchLiftJsonProject = Project(
    id = "dispatch-lift-json",
    base = file("."),
    settings = defaultSettings ++ publishSettings
  )

  lazy val publishSettings:Seq[Setting[_]] = Seq(
    publishSetting
  ) ++ credsSetting

  def validFile(f:File):Either[String, File] =
    if (f.exists)
      Right(f)
    else
      Left(f.toString + " not found")

  // Like Option's orElse, but accumulate error messages
  def orElse[F, S](v1:Either[F, S], v2:Either[F, S]):Either[List[F], S] =
    (v1, v2) match {
      case (Right(fst), _) => Right(fst)
      case (_, Right(snd)) => Right(snd)
      case (Left(f1), Left(f2)) => Left(List(f1, f2))
    }
  
  lazy val credsSetting:Option[Setting[_]] = creds match{
    case Left(errors) =>
      println("No publishing credentials: " + errors.mkString(", "))
      None
    case Right(creds) =>
      Some(credentials += creds)
  }

  lazy val creds:Either[List[String], Credentials] = {

    // This will either be valid Credentials, or a String of an error message why not.
    val homeCreds:Either[String, Credentials] =
      validFile(Path.userHome / ".ivy2" / ".credentials").right.map( Credentials(_) )

    // Ditto, only this can go wrong in two places instead of just one.
    val cloudbeesCreds:Either[String, Credentials] = for {
      mvnSettings <- validFile(Path.userHome / ".m2" / "settings.xml").right
      server <- (XML.loadFile(mvnSettings) \ "servers" \ "server").find(
          _.\("id").text == "cloudbees-private-repository"
        ).toRight(
          "No 'server' element with id of 'cloudbees-private-repository' found"
        ).right
    } yield Credentials(
      "janrain repository",
      "repository-janrain.forge.cloudbees.com",
      server \ "username" text,
      server \ "password" text
    )

    orElse(homeCreds, cloudbeesCreds)
  }

  val cloudbees = "https://repository-janrain.forge.cloudbees.com/"

  val publishSetting = publishTo <<= version {v => Some(
    if (v.trim endsWith "SNAPSHOT")
      "snapshot" at cloudbees + "snapshot/"
    else
      "release" at cloudbees + "release/"
  )}

}
