package Agittex.StatClients.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {
    private Long id;
    private String nom;
    private String prenom;
    private Integer age;
    private String profession;
    private Double salaire;
}
