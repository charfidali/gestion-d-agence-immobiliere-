package pi.app.estatemarket.Services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pi.app.estatemarket.Entities.Contract;
import pi.app.estatemarket.Entities.UserApp;
import pi.app.estatemarket.Repository.ContractRepository;
import pi.app.estatemarket.Repository.UserRepository;
import pi.app.estatemarket.dto.ContractDTO;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j

public class ImplContractService implements IContractService {
   // @Autowired
    private final ContractRepository contractRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    @Autowired
    JavaMailSender javaMailSender;

    public List<ContractDTO> getAllContracts() {
        List<Contract> contracts = contractRepository.findAll();
        return getAllContracts().stream()
                .map(contract -> modelMapper.map(contract, ContractDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Contract updateContract(Contract contract)
    {
        return contractRepository.save(contract);
    }

    @Override
    public void DeleteContract(Integer idC) {
        contractRepository.deleteById(idC);
    }
    @Override
    public Contract retrieveContract(Integer idC)
    {
        return contractRepository.findById(idC).orElse(null);
    }

    @Override
    public void addaffectContractToUser(Contract contract, Long userID) {
        UserApp user = userRepository.findById(userID).orElse(null);
        contract.setUserAppContract(user);
        contractRepository.save(contract);


        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("schoolesprit1@gmail.com");

        //simpleMailMessage.setTo(user.getEmailAddress());

        simpleMailMessage.setTo("yossr.boushih@esprit.tn");
        simpleMailMessage.setSubject("Nouveau Contrat !");
        simpleMailMessage.setText(" cher Admin du site Estate Markest vous avez ajouter un nouveau contrat");
        javaMailSender.send(simpleMailMessage);
    }

    @Override
    public String isContractActive(int id) {
        Contract contract = contractRepository.findById(id).orElse(null);
        if(contract != null) {
            Date currentDate = new Date();
            long diffInMillies = Math.abs(currentDate.getTime() - contract.getEndDateContract().getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if (contract.getEndDateContract().after(currentDate)) {
                return "Contract is active";
            } else {
                return "Contract has been expired for " + diffInDays + " days";
            }
        }
        return "Contract not found";
    }

}



