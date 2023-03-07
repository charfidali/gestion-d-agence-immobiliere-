package pi.app.estatemarket.Controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pi.app.estatemarket.Entities.Contract;
import pi.app.estatemarket.Services.IContractService;
import pi.app.estatemarket.Services.ImplContractService;
import pi.app.estatemarket.dto.ContractDTO;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/api/contract")
@AllArgsConstructor

public class ContractController {

    //@Autowired
    private IContractService iContractService;
@PostMapping("/addaffectContractToUser/{userID}")
    public void addaffectContractToUser( @RequestBody Contract contract, @PathVariable Long userID){
    iContractService.addaffectContractToUser(contract,userID);


    }
@DeleteMapping("/deletecontract/{idC}")
public void DeleteContract( @PathVariable Integer idC){

    iContractService.DeleteContract(idC);
}
@PutMapping("/updatecontract")
public Contract updateContract( @RequestBody Contract contract){

    return iContractService.updateContract(contract);
}
@GetMapping("/affichageducontrat/{idC}")
    public Contract retrieveContract( @PathVariable  Integer idC){
    return iContractService.retrieveContract(idC);
    }




    @GetMapping("/{id}/active")
    public String isContractActive(@PathVariable int id) {
        return iContractService.isContractActive(id);
    }
    @GetMapping("/touslescontrats")
    public List<ContractDTO> getAllContracts(){

    return iContractService.getAllContracts();
    }

}



