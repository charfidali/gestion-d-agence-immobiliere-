package pi.app.estatemarket.Services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pi.app.estatemarket.Entities.Comment;
import pi.app.estatemarket.Entities.Publication;
import pi.app.estatemarket.Entities.User;
import pi.app.estatemarket.Repository.CommentRepository;
import pi.app.estatemarket.Repository.PublicationRepository;
import pi.app.estatemarket.Repository.UserRepository;
import pi.app.estatemarket.dto.CommentDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ServiceComment implements IServiceComment{
    CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    PublicationRepository publicationRepository;
    UserRepository userRepository;

   /* @Override
    public List<Comment> retrieveAll
    Comments() {
        return commentRepository.findAll();
    } */

    @Override
    public List<CommentDTO> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return comments.stream()
                .map(comment -> modelMapper.map(comment, CommentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Comment updateComment(Comment comm) {
        return commentRepository.save(comm);
    }

    @Override
    public Comment addComment(Comment comm) {
        return commentRepository.save(comm);
    }

    @Override
    public Comment retrieveComment(Integer IdComment) {
        return commentRepository.findById(IdComment).get();
    }

    @Override
    public void removeComment(Integer IdComment) {
        commentRepository.deleteById(IdComment);
    }

    @Override
    public void AffectPubToComment(int IdComment, int IdPublication) {
        Comment comment = commentRepository.findById(IdComment).orElse(null);
        Publication publication = publicationRepository.findById(IdPublication).orElse(null);
        comment.setCommPub(publication);
        commentRepository.save(comment);
    }

    @Override
    public void ajouterEtAffecterCommentaireAUserEtCommentaire(Comment comment, Long userID, int IdPublication) {
        User user = userRepository.findById(userID).orElse(null);
        Publication publication = publicationRepository.findById(IdPublication).orElse(null);
        comment.setCommPub(publication);
        comment.setUserComment(user);
        commentRepository.save(comment);

    }

    @Override
    public List<Comment> getAllFilteredComments() {
        return commentRepository.findAllFiltered().stream()
                .peek(comment -> comment.setDescriptionCommentaire(comment.getFilteredDescriptionCommentaire()))
                .collect(Collectors.toList());
    }
}
