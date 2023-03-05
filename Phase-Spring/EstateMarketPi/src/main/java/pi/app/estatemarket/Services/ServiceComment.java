package pi.app.estatemarket.Services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pi.app.estatemarket.Entities.Comment;
import pi.app.estatemarket.Entities.Publication;
import pi.app.estatemarket.Entities.UserApp;


import pi.app.estatemarket.Repository.CommentRepository;
import pi.app.estatemarket.Repository.PublicationRepository;
import pi.app.estatemarket.Repository.UserRepository;
import pi.app.estatemarket.dto.CommentDTO;

//import pi.app.estatemarket.dto.PublicationDTO;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceComment implements IServiceComment {
    CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    PublicationRepository publicationRepository;
    UserRepository userRepository;


    @Override
    public List<CommentDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Comment updateComment(int IdComment, Comment comm) throws Exception{
        return commentRepository.findById(IdComment)
                .map(comment1 -> {
                    comment1.setDescriptionCommentaire(comm.getDescriptionCommentaire());
                    return commentRepository.save(comment1);
                }).orElseThrow(() -> new RuntimeException("Comment not found !"));
}
    @Override
    public Comment retrieveComment(Integer IdComment) throws Exception {
        Optional<Comment> commentOptional = commentRepository.findById(IdComment);
        if (!commentOptional.isPresent()) {
            throw new Exception("Comment with ID " + IdComment + " does not exist.");
        }
        return commentOptional.get();
    }


    @Override
    public void removeComment(Integer IdComment) throws Exception {
        if (!commentRepository.existsById(IdComment)) {
            throw new Exception("Comment with ID " + IdComment + " does not exist.");
        }
        commentRepository.deleteById(IdComment);
    }


    @Override
    public void ajouterEtAffecterCommentaireAUserEtCommentaire(Comment comment, Long userID, int IdPublication) throws Exception {
        UserApp user = userRepository.findById(userID).orElseThrow(() -> new Exception("User with ID " + userID + " does not exist."));
        Publication publication = publicationRepository.findById(IdPublication).orElseThrow(() -> new Exception("Publication with ID " + IdPublication + " does not exist."));
        comment.setCommPub(publication);
        comment.setUserAppComment(user);
        String descriptionFiltree = filtrerMotsInterdits(comment.getDescriptionCommentaire());
        comment.setDescriptionCommentaire(descriptionFiltree);
        commentRepository.save(comment);

    }
    private String filtrerMotsInterdits(String descriptionCommentaire) {
        // Liste de mots interdits
        List<String> motsInterdits = Arrays.asList("insulte", "haine", "racisme");

        for (String motInterdit : motsInterdits) {
            descriptionCommentaire = descriptionCommentaire.replaceAll(motInterdit, "***");
        }

        return descriptionCommentaire
                ;
    }

    @Override
    public Comment reportComment(int idComment, long userId) throws Exception {
        Optional<Comment> commentOptional = commentRepository.findById(idComment);
        if (!commentOptional.isPresent()) {
            throw new Exception("Comment with ID " + idComment + " does not exist.");
        }

        if (!userRepository.existsById(userId)) {
            throw new Exception("User with ID " + userId + " does not exist.");
        }

        Comment comment = commentOptional.get();
        if (comment.getUserAppComment().getUserID() == userId) {
            throw new Exception("You cannot report your own comment");
        }

        if (comment.getReportedBy().contains(userId)) {
            throw new Exception("You have already reported this comment");
        }

        comment.getReportedBy().add(userId);
        comment.setSignalCount(comment.getSignalCount() + 1);
        if (comment.getSignalCount() >= 3) {
            commentRepository.delete(comment);
            return null;
        } else {
            return commentRepository.save(comment);
        }
    }
}





//--------------------------------------------
/*
    @Override
    public Comment addComment(Comment comm) {
        return commentRepository.save(comm);
    }
*/

/*    @Override
    public List<Comment> getAllFilteredComments() {
        return commentRepository.findAllFiltered().stream()
                .peek(comment -> comment.setDescriptionCommentaire(comment.getFilteredDescriptionCommentaire()))
                .collect(Collectors.toList());
    }*/

    /*   @Override
    public void AffectPubToComment(int IdComment, int IdPublication) {
        Comment comment = commentRepository.findById(IdComment).orElse(null);
        Publication publication = publicationRepository.findById(IdPublication).orElse(null);
        comment.setCommPub(publication);
        commentRepository.save(comment);
    }
*/

   /* @Override
    public List<Comment> retrieveAll
    Comments() {
        return commentRepository.findAll();
    } */


