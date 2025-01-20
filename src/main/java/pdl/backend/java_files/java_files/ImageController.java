package pdl.backend.java_files.java_files;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Optional;

@RestController
public class ImageController {

  @Autowired
  private ObjectMapper mapper;

  private final ImageDao imageDao;

  @Autowired
  public ImageController(ImageDao imageDao) {
    this.imageDao = imageDao;
  }

  @RequestMapping(value = "/images/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<?> getImage(@PathVariable("id") long id) {
    return ResponseEntity
            .ok()
            .contentType(MediaType.IMAGE_JPEG)
            .body(imageDao.retrieve(id).get().getData().clone());
  }

  @RequestMapping(value = "/images/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteImage(@PathVariable("id") long id) throws JsonProcessingException {
    imageDao.delete(imageDao.retrieve(id).get());
    String reponse =  "/"+imageDao.retrieve(id).get().getName()+"/"+id+"-> ajout d'une image au format JPEG";
    return ResponseEntity
            .ok(mapper.writeValueAsString(reponse));
  }

  @RequestMapping(value = "/images", method = RequestMethod.POST)
  public ResponseEntity<?> addImage(@RequestParam("file") MultipartFile file,
      RedirectAttributes redirectAttributes) {
    try {
      Optional <Image> image = Optional.of(new Image(file.getName(),file.getBytes()));
      String reponse =  imageDao.retrieve(image.get().getId()).get().getName() +"-> ajout d'une image au format JPEG";
      imageDao.create(image.get());
      return  ResponseEntity
              .ok(mapper.writeValueAsString(reponse));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @RequestMapping(value = "/images", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  @ResponseBody
  public ArrayNode getImageList() {
    ArrayNode nodes = mapper.createArrayNode();
    // TODO
    return nodes;
  }

}
