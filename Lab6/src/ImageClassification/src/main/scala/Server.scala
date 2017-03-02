import java.io.{ByteArrayInputStream, File}
import java.net.InetSocketAddress
import javax.imageio.ImageIO

import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}
import sun.misc.BASE64Decoder

/**
  * Created by AC010168 on 3/1/2017.
  */
object Server extends App {
  val server = HttpServer.create(new InetSocketAddress(8080), 0)
  server.createContext("/get_custom", new RootHandler())
  server.setExecutor(null)
  server.start()
  println("------ waiting for Request ------")
}

class RootHandler extends HttpHandler {
  def handle(httpExchange: HttpExchange) {
    val data = httpExchange.getRequestBody

    //Commenting out.  Just receiving the text
    //val imageByte = (new BASE64Decoder()).decodeBuffer(data);
    //val bytes = new ByteArrayInputStream(imageByte)
    //val image = ImageIO.read(bytes)
    //ImageIO.write(image, "png", new File("image.png"))

    val bodyContent = scala.io.Source.fromInputStream(data).mkString
    println("------ Image receiving complete ------")

    //TODO - Get a response here
    val result1 = ImageClassification_DT.testSpecificImage(bodyContent);
    val result2 = ImageClassification_RF.testSpecificImage(bodyContent);

    val response = result1 + "\n" + result2;

    httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*")
    httpExchange.sendResponseHeaders(200, response.length())
    val outStream = httpExchange.getResponseBody
    outStream.write(response.getBytes)
    outStream.close()
  }
}
