import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws JsonProcessingException, SQLException {

        String jdbcURL = "jdbc:mysql://35.199.97.222:3306/catbase";
        String username = "root";
        String password = "CAT154cat";

        Connection connection = DriverManager.getConnection(jdbcURL, username, password);
        Statement st = connection.createStatement();

        HttpResponse<String> response =
                Unirest.get("https://api.thecatapi.com/v1/breeds?attach_breed=0").header("x" + "-api-key",
                        "80e62e7b" + "-2742-46c1-820b-06e8245a3935").asString();

        ObjectMapper mapper = new ObjectMapper();

        List<Caracteristicas> catsJsonList = mapper.readValue(response.getBody(), new TypeReference<>() {
        });

        /*
        catsJsonList.forEach(caracteristicas -> System.out.println("INSERT INTO `Racas`(raca, origem, temperamento, descricao) values ('" + caracteristicas.getName() + "', '" +
                caracteristicas.getOrigin() + "', '" +
                caracteristicas.getTemperament() + "', '" +
                caracteristicas.getDescription() + "');"));

         */

        catsJsonList.forEach(caracteristicas -> {

            String query = "INSERT INTO `Gatos`(raca, origem, temperamento, descricao) values (?, ?, ?, ?)";
            PreparedStatement statement= null;
            try {
                statement = connection.prepareStatement(query);
                statement.setString(1, caracteristicas.getName());
                statement.setString(2, caracteristicas.getOrigin());
                statement.setString(3, caracteristicas.getTemperament());
                statement.setString(4, caracteristicas.getDescription());
                System.out.println(statement);
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            /*
            try {
                st.executeUpdate(String.format('INSERT INTO `Gatos`(raca, origem, temperamento, descricao) values ("%s");', caracteristicas.getName()));
            } catch (SQLException e) {
                e.printStackTrace();
            }

             */
        });
        
        //TODO salvar as raças no banco

        //Salva origem, temperamento e descrição de cada raça
        catsJsonList.forEach(caracteristicas -> {
            caracteristicas.getOrigin();
            caracteristicas.getTemperament();
            caracteristicas.getDescription();
            //TODO salvar dados no banco
        });

        //Salva 3 fotos de cada raça
        catsJsonList.forEach(caracteristicas -> {

            HttpResponse<String> response2 =
                    Unirest.get("https://api.thecatapi.com/v1/breeds?attach_breed=" + caracteristicas.getName()).header("x-api-key", "80e62e7b-2742-46c1-820b-06e8245a3935").asString();

            List<Caracteristicas> imageCatJsonList = null;
            List<String> fotos = new ArrayList<>();

            try {
                imageCatJsonList = mapper.readValue(response2.getBody(), new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            for (int i = 0; i <= 2; i++) {
                fotos.add(imageCatJsonList.get(i).getImage().getUrl());
            }

            //TODO salvar fotos no banco

        });

        //Salva 3 fotos com chápeu
        HttpResponse<String> response3 = Unirest.get("https://api.thecatapi.com/v1/images/search?limit=3&category_ids"
                + "=1").header("x-api-key", "80e62e7b-2742-46c1-820b-06e8245a3935").asString();

        List<Imagens> hatsJsonList = (mapper.readValue(response3.getBody(), new TypeReference<>() {
        }));

        //TODO salvar as fotos no banco

        //Salva 3 fotos com óculos
        HttpResponse<String> response4 = Unirest.get("https://api.thecatapi.com/v1/images/search?limit=3&category_ids"
                + "=4").header("x-api-key", "80e62e7b-2742-46c1-820b-06e8245a3935").asString();

        List<Imagens> glassesJsonList = (mapper.readValue(response4.getBody(), new TypeReference<>() {
        }));

        //TODO salvar as fotos no banco

    }

}
