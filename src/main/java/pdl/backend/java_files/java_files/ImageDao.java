package pdl.backend.java_files.java_files;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

@Repository
public class ImageDao implements Dao<Image> {

  private final Map<Long, Image> images = new HashMap<>();

  public ImageDao() {
    // placez une image test.jpg dans le dossier "src/main/resources" du projet
    final ClassPathResource imgFile = new ClassPathResource("test.jpg");
    byte[] fileContent;
    try {
      fileContent = Files.readAllBytes(imgFile.getFile().toPath());
      Image img = new Image("logo.jpg", fileContent);
      images.put(img.getId(), img);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Optional<Image> retrieve(final long id) {
    Image img = images.get(id);
    return Optional.ofNullable(img);
  }

  @Override
  public List<Image> retrieveAll() {
    ArrayList<Image> listImage = new ArrayList<Image>();
    images.forEach((id,img)->{listImage.add(img);});
    return listImage;
  }

  @Override
  public void create(final Image img) {
    if(images.containsKey(img.getId()-1)) images.put(img.getId(), img);
    images.put(img.getId()-1, img);
  }

  @Override
  public void update(final Image img, final String[] params) {
    // Not used
  }

  @Override
  public void delete(final Image img) {
    images.remove(img.getId(),img);
  }
}
