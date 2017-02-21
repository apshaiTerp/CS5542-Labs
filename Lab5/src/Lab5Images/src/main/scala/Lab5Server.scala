import unfiltered.jetty.SocketPortBinding

/**
  * Created by AC010168 on 2/20/2017.
  */
object Lab5Server extends App {
  val bindingIP = SocketPortBinding(host = "127.0.0.1", port = 8080)
  unfiltered.jetty.Server.portBinding(bindingIP).plan(Lab5Plan).run()
}
