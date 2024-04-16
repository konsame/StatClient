package Agittex.StatClients.services.implement;
import Agittex.StatClients.dto.ClientDto;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class UploadServiceImpl {

    @Autowired
    private final ClientServiceImpl clientService;

    public void defineFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            if (fileName.endsWith(".txt")) {
                processFile(file.getInputStream());
            }else if(fileName.endsWith(".json")){
                processJsonFile(file.getInputStream());
            }else if(fileName.endsWith(".csv")){
                handleCsvFile(file.getInputStream());
            }else if(fileName.endsWith(".xml")){
                processXmlFile(file.getInputStream());
            }
            else {
                throw new IllegalArgumentException("Type de fichier non pris en charge : ");
            }
        }
        else {
            throw new IllegalArgumentException("Format du fichier non valide");
        }
    }




    public void processFile(InputStream inputStream) throws IOException {
        List<ClientDto> clients = readFileContentFromInputStream(inputStream);
        for (ClientDto clientDto : clients) {
            clientService.create(clientDto);
        }
    }
    private List<ClientDto> readFileContentFromInputStream(InputStream inputStream) throws IOException {
        List<ClientDto> clients = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String nom = parts[0].trim();
                    String prenom = parts[1].trim();
                    int age = Integer.parseInt(parts[2].trim());
                    String profession = parts[3].trim();
                    String salaireString = parts[4].trim().replaceAll("[^0-9.]", "");
                    double salaire = Double.parseDouble(salaireString);

                    ClientDto clientDto = new ClientDto();
                    clientDto.setNom(nom);
                    clientDto.setPrenom(prenom);
                    clientDto.setAge(age);
                    clientDto.setProfession(profession);
                    clientDto.setSalaire(salaire);
                    clients.add(clientDto);
                }
            }
        }
        return clients;
    }


    public void handleCsvFile(InputStream inputStream) throws IOException {
        List<ClientDto> clients = processCsvFile(inputStream);
        for (ClientDto clientDto : clients) {
            clientService.create(clientDto);
        }
    }
    public List<ClientDto> processCsvFile(InputStream inputStream) throws IOException {
        List<ClientDto> clients = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String headerLine = br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String nom = parts[0].trim();
                    String prenom = parts[1].trim();
                    String ageStr = parts[2].trim();
                    String profession = parts[3].trim();
                    String salaireString = parts[4].trim().replaceAll("[^0-9.]", "");
                    int age = 0;
                    try {
                        age = Integer.parseInt(ageStr);
                    } catch (NumberFormatException e) {
                        System.err.println("Erreur de conversion de l'âge pour la ligne : " + line);
                        continue;
                    }
                    double salaire = Double.parseDouble(salaireString);

                    ClientDto clientDto = new ClientDto();
                    clientDto.setNom(nom);
                    clientDto.setPrenom(prenom);
                    clientDto.setAge(age);
                    clientDto.setProfession(profession);
                    clientDto.setSalaire(salaire);
                    clients.add(clientDto);
                    clientService.create(clientDto);
                }
            }
        }
        return clients;
    }


    public void processJsonFile(InputStream inputStream) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<ClientDto> clients = objectMapper.readValue(inputStream, new TypeReference<List<ClientDto>>() {});
        for (ClientDto clientDto : clients) {
            clientService.create(clientDto);
        }
    }


    public void processXmlFile(InputStream inputStream) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        List<ClientDto> clients = xmlMapper.readValue(inputStream, new TypeReference<List<ClientDto>>() {});
        for (ClientDto clientDto : clients) {
            clientService.create(clientDto);
        }
    }




}
