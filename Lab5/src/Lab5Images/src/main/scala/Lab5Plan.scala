import java.io.{ByteArrayInputStream, File}
import javax.imageio.ImageIO

import unfiltered.filter.Plan
import _root_.unfiltered.request.{Body, GET, POST, Path}
import sun.misc.BASE64Decoder
import unfiltered.response.{Ok, ResponseString}

/**
  * Created by AC010168 on 2/20/2017.
  */
object Lab5Plan extends Plan {

  def intent = {
    case req@GET(Path("/get")) => {
      Ok ~> ResponseString(ServerSupport.testImage("data/test/dc/vrcap_8622724.jpg"))
    }

    case req@POST(Path("/get_custom")) => {
      try {
        val imageByte = (new BASE64Decoder()).decodeBuffer(Body.string(req));
        val bytes = new ByteArrayInputStream(imageByte)
        val image = ImageIO.read(bytes);
        ImageIO.write(image, "png", new File("data/web/image.png"))
      } catch {
        case e : Exception => println (e.getMessage());
          e.printStackTrace();
      }
      Ok ~> ResponseString(ServerSupport.testImage("data/web/image.png"))
    }
  }
}
