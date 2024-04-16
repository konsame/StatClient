package Agittex.StatClients.services.interfaces;

import Agittex.StatClients.dto.ClientDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public interface ClientService {
    ClientDto create(ClientDto client);
    List<ClientDto> list ();
    ClientDto update (ClientDto client);
    void delete (Long id);
    String statistique (String profession);

}

