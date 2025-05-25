package it.uniroma3.siw.siwbooks.service;
import it.uniroma3.siw.siwbooks.model.Image;
import it.uniroma3.siw.siwbooks.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Transactional
    public Image saveImage(Image image) {
        return imageRepository.save(image);
    }

    @Transactional
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Image getImage(Long id) {
        return imageRepository.findById(id).orElse(null);
    }
}
