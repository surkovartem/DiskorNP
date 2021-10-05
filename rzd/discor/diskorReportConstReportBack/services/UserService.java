package ru.rzd.discor.diskorReportConstReportBack.services;

import ru.rzd.discor.diskorReportConstReportBack.Main;
import ru.rzd.discor.diskorReportConstReportBack.SetFiltersException;
import ru.rzd.discor.diskorReportConstReportBack.models.user.User;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class UserService {
    public User getUser(String token) {
        String userObjectString = null;
        try {
            userObjectString = decodeToken(token);
        } catch (Exception e) {
            //throw new SetFiltersException("Ошибка при формировании строки объекта пользователя из токена", e);
            System.out.println("Файл с токеном пуст, ошибка формирования строки объекта пользователя из токена.");
        }
        User user = null;
        try {
            user = Main.objectMapper.readValue(userObjectString, User.class);
        } catch (Exception e) {
            //throw new SetFiltersException("Ошибка при маппинге строки объекта пользователя");
            System.out.println("Ошибка при маппинге строки объекта пользователя.");
        }
        return user;
    }

    public Boolean checkRoadCode(Integer userRoadCode, Integer roadCodeToCheck) {
        return userRoadCode != null && userRoadCode.equals(roadCodeToCheck);
    }

    private String decodeToken(String token)
    {
        String decodedString = "";
        String cutToken = token.substring("Bearer ".length());
        String[] parts = cutToken.split("\\.", 0);

        for (String part : parts) {
            byte[] bytes = Base64.getUrlDecoder().decode(part);
            decodedString += new String(bytes, StandardCharsets.UTF_8);
        }
        //Берем второй объект JSON, так как первый объект включает информацию не о пользователе, а о ключе
        decodedString = decodedString.substring(decodedString.indexOf("{",1));
        return decodedString;
    }
}