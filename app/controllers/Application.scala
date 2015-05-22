package controllers

import akka.actor.{Actor, ActorSystem, Inbox, Props}
import play.api.mvc._
import scala.concurrent.duration._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def hello(name: String) = Action {
    val system = ActorSystem()

    val inbox = Inbox.create(system)
    val helloActor = system.actorOf(Props[HelloActor])

    inbox.send(helloActor, name)
    val greet = inbox.receive(5.seconds)
    Ok("Hola " + greet)
  }
}

class HelloActor extends Actor {
  def receive = {
    case n:String => println(n); sender ! "Hello, " + n + "!!";
    case _       => println("huh?")
  }
}