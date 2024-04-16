package Agittex.StatClients.services.implement;

import Agittex.StatClients.dto.ClientDto;
import Agittex.StatClients.entities.Client;
import Agittex.StatClients.repositories.ClientRepository;
import Agittex.StatClients.services.interfaces.ClientService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;


    public ClientDto create(ClientDto client){
        System.out.println("Données a enregistrer : " + client);
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
