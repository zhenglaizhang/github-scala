package net.zhenglai
package playzio

import zio.{ExitCode, Has, Task, URIO, ZIO, ZLayer}

object HelloZio extends zio.App {
  val success = ZIO.succeed(32)
  val fail = ZIO.fail("Boom")

  import zio.console._

  val greetingZio =
    for {
      _ <- putStrLn("what's your name?")
      name <- getStrLn
      _ <- putStrLn(s"hello $name, scala is very cool")
    } yield ()

  // These ZIO instances don’t actually do anything; they only describe what will be computed or “done”.
  def run(args: List[String]): URIO[Console, ExitCode] = greetingZio.exitCode
}

// ZIO matches service pattern (db layer, business logic layer, api layer, communicating with other services) perfectly:
//  - a service may have dependencies, therefore “inputs” or “environment”
//  - a service may fail with an error
//  - a service, once created, may serve as dependency or input to other services
// => ZLayer pattern
case class User(name: String, email: String)

object UserEmailer {
  type UserEmailEnv = Has[UserEmailer.Service]

  trait Service {
    // A Task is an alias for ZIO[Any, Throwable, A]: produces a value (of type Unit in this case), takes no inputs
    // and can throw an exception
    def notify(user: User, message: String): Task[Unit]
  }

  // Much like ZIO, a ZLayer has 3 type arguments:
  //  - an input type RIn, aka “dependency” type
  //  - an error type E, for the error that might arise during creation of the service
  //  - an output type ROut
  // layer; including service implementation
  val live: ZLayer[Any, Nothing, Has[UserEmailer.Service]] = ZLayer.succeed(
    new Service {
      override def notify(user: User, message: String): Task[Unit] = Task {
        println(s"Sending '$message' to ${user.email}")
      }
    }
  )

  // front-facing API, aka "accessor"
  def notify(u: User, msg: String): ZIO[UserEmailEnv, Throwable, Unit] = {
    ZIO.accessM(_.get.notify(u, msg))
    // The notify method is an effect, so it’s a ZIO instance. The input type is a Has[UserEmailer.Service], which
    // means that whoever calls this notify method needs to have obtained a UserEmailer.Service
  }
}

object UserEmailerPlayground extends zio.App {
  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] =
    UserEmailer
      .notify(User("zzl", "zzl@email.com"), "welcome to scala world") // the specification of the action
      .provideLayer(UserEmailer.live) // plugging in a real layer/implementation to run on
      .exitCode // trigger the effect
}

// Another ZLayer for user email db
object UserDb {
  type UserDbEnv = Has[UserDb.Service]

  trait Service {
    def insert(user: User): Task[Unit]
  }

  val live: ZLayer[Any, Nothing, UserDbEnv] = ZLayer.succeed {
    new Service {
      override def insert(user: User): Task[Unit] = Task {
        println(s"[Database] inserting into public.user values (`${user.name}`)")
      }
    }
  }

  // accessor
  def insert(u: User): ZIO[UserDbEnv, Throwable, Unit] = ZIO.accessM(_.get.insert(u))
}

// Composing ZLayers
//  - we can compose ZLayer instances like functions.
//  - “horizontal” composition. If we have
//    - a ZLayer[RIn1, E1, ROut1]
//    - another ZLayer[RIn2, E2, ROut2]
//    we can obtain a “bigger” ZLayer which can take as input RIn1 with RIn2, and produce as output ROut1 with ROut2.
//    If
//    we suggested earlier that RIn is a “dependency”, then this new ZLayer combines (sums) the dependencies of both
//    ZLayers, and produces a “bigger” output, which can serve as dependency for a later ZLayer.
// val userBackendLayer: ZLayer[Any, Nothing, UserDbEnv with UserEmailerEnv] =
//  UserDb.live ++ UserEmailer.live

object HorizontalComposition extends zio.App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = ???
}

object UserSubscription {

  import UserEmailer._
  import UserDb._

  type UserSubscriptionEnv = Has[UserSubscription.Service]

  // Concrete instances of UserEmailer.Service and UserDb.Service will in turn influence the instances of
  // UserSubscription.Service via — you guessed it — dependency injection
  class Service(notifier: UserEmailer.Service, userModel: UserDb.Service) {
    // “vertical” composition, which is more akin to regular function composition: the output of one ZLayer is the
    // input of another ZLayer, and the result becomes a new ZLayer with the input from the first and the output from
    // the second
    def subscribe(u: User): Task[User] = {
      for {
        _ <- userModel.insert(u)
        _ <- notifier.notify(u, s"Welcome, ${u.name}")
      } yield u
    }
  }

  // Why Has[...]
  //  an instance of Has[Service1] with Has[Service2] has one instance of Service1 and one instance of Service2,
  //  which we can surface and use independently, instead of a composite Service1 with Service2 instance.

  // layer with service implementation via dependency injection
  // Combine UserDb and UserEmailer horizontally, because they have no dependencies
  val live: ZLayer[UserEmailEnv with UserDbEnv, Nothing, UserSubscriptionEnv] =
  ZLayer.fromServices[UserEmailer.Service, UserDb.Service, UserSubscription.Service] { (emailer, db) =>
    new Service(emailer, db)
  }

  // accessor
  def subscribe(u: User): ZIO[UserSubscriptionEnv, Throwable, User] = ZIO.accessM(_.get.subscribe(u))
}

object ZLayerPlayground extends zio.App {
  override def run(args: List[String]): ZIO[zio.ZEnv, Nothing, ExitCode] = {
    val userRegistrationLayer = (UserDb.live ++ UserEmailer.live) >>> UserSubscription.live
    UserSubscription.subscribe(User("zzl", "zzl@email.com"))
      .provideLayer(userRegistrationLayer)
      .catchAll(t => ZIO.succeed(t.printStackTrace()).map(_ => ExitCode.failure))
      .map { user =>
        println(s"Registered user: $user")
        ExitCode.success
      }
  }
}