package Agittex.StatClients.services.implement;

import Agittex.StatClients.dto.ClientDto;
import Agittex.StatClients.entities.Client;
import Agittex.StatClients.repositories.ClientRepository;
import Agittex.StatClients.services.interfaces.ClientService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;


    public ClientDto create(ClientDto client){
        return toDto(clientRepository.save(toEntity(client)));
    }

    public List<ClientDto> list (){
        return clientRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ClientDto update (ClientDto client){
        return toDto(clientRepository.save(toEntity(client)));
    }

    public void delete (Long id){
        clientRepository.deleteById(id);
    }


    public String statistique (String profession){
        int count = 0;
        Double totalSalaire = 0.0;

        List<Client> clientProfession = clientRepository.findByProfession(profession);
        if (clientProfession !=null){
            for (Client client : clientProfession){
                count ++;
                totalSalaire += client.getSalaire();
            }
            Double moyenne = totalSalaire / count;
            return "La moyenne des salaires pour la profession " + profession + " est : " + moyenne;
        }
        else {
            return "Il n'existe aucun client ayant la profession: " + profession ;
        }
    }


    public void processFile(InputStream inputStream) throws IOException {
        ClientDto clientDto = readFileContentFromInputStream(inputStream);
        System.out.println("Contenu du fichier dto : " + clientDto);
        create(clientDto);
    }
    private ClientDto readFileContentFromInputStream(InputStream inputStream) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            ClientDto clientDto = new ClientDto();
            while ((line = br.readLine()) != null) {
                System.out.println("Ligne lue du fichier : " + line);
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String nom = parts[0].trim();
                    String prenom = parts[1].trim();
                    int age = Integer.parseInt(parts[2].trim());
                    String profession = parts[3].trim();
                    double salaire = Double.parseDouble(parts[4].trim());
                    clientDto.setNom(nom);
                    clientDto.setPrenom(prenom);
                    clientDto.setAge(age);
                    clientDto.setProfession(profession);
                    clientDto.setSalaire(salaire);
                }
            }
            return clientDto;
        }
    }


    public ClientDto toDto (Client client){
        ClientDto clientDto = modelMapper.map(client , ClientDto.class);
        return clientDto;
    }

    public Client toEntity (ClientDto clientDto){
        Client clientEntity = new Client();
        clientEntity = modelMapper.map(clientDto, Client.class);
        return clientEntity;
    }
}
