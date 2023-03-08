package pi.app.estatemarket.Services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pi.app.estatemarket.Entities.Comment;
import pi.app.estatemarket.Entities.Likee;
import pi.app.estatemarket.Entities.Publication;
import pi.app.estatemarket.Entities.UserApp;
import pi.app.estatemarket.Repository.LikeRepository;
import pi.app.estatemarket.Repository.PublicationRepository;
import pi.app.estatemarket.Repository.UserRepository;

import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class ServicePublication implements IServicePublication {
    private final ModelMapper modelMapper;
    PublicationRepository publicationRepository;
    UserRepository userRepository;

    LikeRepository likeRepository;


   /* @Override
    public List<Publication> retrieveAllPublications() {
        return publicationRepository.findAll();
    } */

    @Override
    public List<Publication> getAllPublications() {
        return publicationRepository.findAll();
    }
/*        List<Publication> publications = publicationRepository.findAll();
        return publications.stream()
                .map(publication -> modelMapper.map(publication, PublicationDTO.class))
                .collect(Collectors.toList());
    }*/

    @Override
    public Publication updatePublication(int ID, Publication publication) {
        return publicationRepository.findById(ID)
                .map(publication1 -> {
                    publication1.setDatePublication(publication.getDatePublication());
                    publication1.setDescriptionPublication(publication.getDescriptionPublication());
                    publication1.setTitrePub(publication.getTitrePub());
                    return publicationRepository.save(publication1);
                }).orElseThrow(() -> new RuntimeException("Publication not found !"));
    }

    @Override
    public Publication retrievePublication(Integer IdPublication) {
        Publication post = publicationRepository.findById(IdPublication).orElse(null);
        post.incrementViews(); // incrémente le nombre de vues à chaque consultation
        publicationRepository.save(post);
        return post;
    }

    @Override
    public void removePublication(Integer IdPublication) {
        publicationRepository.deleteById(IdPublication);
    }

    @Override
    public Publication addAndAffectPublicationTouser(Publication publication, Long userID) throws Exception{

        UserApp user = userRepository.findById(userID).orElse(null);
        publication.setUserAppPub(user);
        for (Publication existingPublication : getAllPublications()) {
            double similarity = new JaroWinklerSimilarity().apply(existingPublication.getDescriptionPublication(), publication.getDescriptionPublication());
            if (similarity > 0.8) { // Set a threshold for similarity
                throw new Exception("A similar publication already exists.");
            }
        }
        return publicationRepository.save(publication);
    }

    @Override
    public Long countCommentsByPublicationId(Integer idPublication) {
        return publicationRepository.countCommentsByPublicationId(idPublication);
    }

    @Override
    public Publication getPublicationWithComments(int id) {
        Optional<Publication> optionalPublication = publicationRepository.findPublicationWithCommentsById(id);
        return optionalPublication.orElseThrow(() -> new RuntimeException("Publication not found"));
    }



    //-----------------------------------------
    @Override
    public void addLikeToPost(Likee likee, Integer idPost, Long idUser) throws Exception {
        Publication post = publicationRepository.findById(idPost).orElse(null);
        UserApp user = userRepository.findById(idUser).orElse(null);
        if (post == null) {
            throw new Exception("Publication with ID " + idPost + " does not exist.");
        }
        if (user == null) {
            throw new Exception("User with ID " + idUser + " does not exist.");
        }
        if (likeRepository.existsByPostAndUserL(post, user)) {
            throw new Exception("User " + idUser + " has already liked the post " + idPost);
        }
        post.setNombreLike(post.getNombreLike() + 1);
        publicationRepository.save(post);
        Likee like = new Likee();
        like.setPost(post);
        like.setUserL(user);
        likeRepository.save(like);
    }


}