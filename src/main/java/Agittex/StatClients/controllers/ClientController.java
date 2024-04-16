package Agittex.StatClients.controllers;

import Agittex.StatClients.dto.ClientDto;
import Agittex.StatClients.services.implement.UploadServiceImpl;
import Agittex.StatClients.services.interfaces.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final UploadServiceImpl uploadService;


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        uploadService.defineFile(file);
        return ResponseEntity.ok("succes");
    }

    @GetMapping("/statistique")
    public ResponseEntity<String> getStatistique(@RequestParam String profession) {
        String resultat = clientService.statistique(profession);
        return ResponseEntity.ok().body(resultat);
    }

    @PostMapping("/create")
    public ResponseEntity<ClientDto> createClient (@RequestBody ClientDto client){
        ClientDto clientToSave = clientService.create(client);
        return ResponseEntity.ok().body(clientToSave);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ClientDto>> getClients(){
        List<ClientDto> clientToList = clientService.list();
        return ResponseEntity.ok().body(clientToList);
    }

    @PutMapping("/update")
    public ResponseEntity<ClientDto> updateClient(@RequestBody ClientDto client){
        ClientDto clientToupdate = clientService.update(client);
        return ResponseEntity.ok().body(clientToupdate);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.ok().build();
    }
}
