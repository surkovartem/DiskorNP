package ru.rzd.discor.diskorReportConstReportBack.services;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;

import org.apache.http.util.EntityUtils;
import ru.rzd.discor.diskorReportConstReportBack.Main;
import ru.rzd.discor.diskorReportConstReportBack.models.numericalPokValue.NumericalPokValue;
import ru.rzd.discor.diskorReportConstReportBack.models.reportRepo.ObjGroup;
import ru.rzd.discor.diskorReportConstReportBack.models.reportRepo.Form;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportRepoService {

    private String createGetRequest(String urlString) throws Exception {
        HttpGet get = new HttpGet(urlString);
        get.setHeader("Authorization", Main.token);
        //Конфигурация по умолчанию
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse result = httpClient.execute(get);

        String resultString = EntityUtils.toString(result.getEntity(), StandardCharsets.UTF_8);
        if (result.getStatusLine().getStatusCode() != 200) {
            throw new Exception(resultString);
        }
        return resultString;
    }

    public Form addForm(File formFile, Form form, List<NumericalPokValue> listNumericalPokValue) throws Exception {
        //Метод отправки
        HttpPost post = new HttpPost(Main.reportRepoApiUrl + "form/add");
        post.setHeader("Authorization", Main.token);
        //Конфигурация по умолчанию
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //#4 Сбор параметров
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        builder.addTextBody("stringFormDto", Main.objectMapper.writeValueAsString(form), contentType);
        builder.addTextBody("stringListNumericalPokValue", Main.objectMapper.writeValueAsString(listNumericalPokValue), contentType);
        builder.addTextBody("reason", "Создание", contentType);
        builder.addBinaryBody("blobOdf", formFile, ContentType.DEFAULT_BINARY, formFile.getName());

        HttpEntity entity = builder.build();
        post.setEntity(entity);
        CloseableHttpResponse result = httpClient.execute(post);
        System.out.println(result.getStatusLine().getStatusCode());

        String resultString = EntityUtils.toString(result.getEntity(), StandardCharsets.UTF_8);
        System.out.println(resultString);
        if (result.getStatusLine().getStatusCode() == 401) {
            throw new Exception("Ошибка авторизации. Заполните токен");
        }
        if (!(result.getStatusLine().getStatusCode() == 200)) {
            throw new Exception(resultString);
        }
        return Main.objectMapper.readValue(resultString, Form.class);
    }

    public Form updateForm(File formFile, Form form, List<NumericalPokValue> listNumericalPokValue) throws Exception {
        //Метод отправки
        HttpPost post = new HttpPost(Main.reportRepoApiUrl + "form/update");
        post.setHeader("Authorization", Main.token);
        //Конфигурация по умолчанию
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //#4 Сбор параметров
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        builder.addTextBody("stringFormDto", Main.objectMapper.writeValueAsString(form), contentType);
        builder.addTextBody("stringListNumericalPokValue", Main.objectMapper.writeValueAsString(listNumericalPokValue), contentType);
        builder.addTextBody("reason", "Обновление", contentType);
        builder.addBinaryBody("blobOdf", formFile, ContentType.DEFAULT_BINARY, formFile.getName());

        HttpEntity entity = builder.build();
        post.setEntity(entity);
        CloseableHttpResponse result = httpClient.execute(post);
        System.out.println(result.getStatusLine().getStatusCode());

        String resultString = EntityUtils.toString(result.getEntity(), StandardCharsets.UTF_8);
        System.out.println(resultString);
        if (result.getStatusLine().getStatusCode() == 401) {
            throw new Exception("Ошибка авторизации. Заполните токен");
        }
        if (!(result.getStatusLine().getStatusCode() == 200)) {
            throw new Exception(resultString);
        }
        return Main.objectMapper.readValue(resultString, Form.class);
    }

    public void setActive(Form form) throws Exception {
        //Метод отправки
        HttpPost post = new HttpPost(Main.reportRepoApiUrl + "form/setActive");
        post.setHeader("Authorization", Main.token);
        //Конфигурация по умолчанию
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //#4 Сбор параметров
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        builder.addTextBody("idForm", form.getId(), contentType);

        HttpEntity entity = builder.build();
        post.setEntity(entity);
        CloseableHttpResponse result = httpClient.execute(post);
        System.out.println(result.getStatusLine().getStatusCode());

        String resultString = EntityUtils.toString(result.getEntity(), StandardCharsets.UTF_8);
        System.out.println(resultString);
        if (result.getStatusLine().getStatusCode() == 401) {
            throw new Exception("Ошибка авторизации. Заполните токен");
        }
        if (!(result.getStatusLine().getStatusCode() == 200)) {
            throw new Exception(resultString);
        }
    }

    public void setInActive(Form form) throws Exception {
        //Метод отправки
        HttpPost post = new HttpPost(Main.reportRepoApiUrl + "form/setInactive");
        post.setHeader("Authorization", Main.token);
        //Конфигурация по умолчанию
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //#4 Сбор параметров
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        builder.addTextBody("idForm", form.getId(), contentType);

        HttpEntity entity = builder.build();
        post.setEntity(entity);
        CloseableHttpResponse result = httpClient.execute(post);
        System.out.println(result.getStatusLine().getStatusCode());

        String resultString = EntityUtils.toString(result.getEntity(), StandardCharsets.UTF_8);
        System.out.println(resultString);
        if (result.getStatusLine().getStatusCode() == 401) {
            throw new Exception("Ошибка авторизации. Заполните токен");
        }
        if (!(result.getStatusLine().getStatusCode() == 200)) {
            throw new Exception(resultString);
        }
    }

    public List<NumericalPokValue> getDataReport(List<NumericalPokValue> listNumericalPokValue, String dateString) throws Exception {
        HttpPost post = new HttpPost(Main.coreApiUrl + "numValues/getDataReport?date=" + dateString);
        post.setHeader("Authorization", Main.token);
        CloseableHttpClient httpClient = HttpClients.createDefault();

        System.out.println("out");
        System.out.println(Main.objectMapper.writeValueAsString(listNumericalPokValue));

        StringEntity entity = new StringEntity(
                Main.objectMapper.writeValueAsString(listNumericalPokValue),
                "application/json",
                "UTF-8");

        post.setEntity(entity);
        CloseableHttpResponse result = httpClient.execute(post);
        System.out.println(result.getStatusLine().getStatusCode());

        String resultString = EntityUtils.toString(result.getEntity(), StandardCharsets.UTF_8);
        System.out.println(resultString);
        if (result.getStatusLine().getStatusCode() == 401) {
            throw new Exception("Ошибка авторизации. Заполните токен");
        }
        if (!(result.getStatusLine().getStatusCode() == 200 || result.getStatusLine().getStatusCode() == 400)) {
            throw new Exception(resultString);
        }
        return new ArrayList<NumericalPokValue>(Arrays.asList(Main.objectMapper.readValue(resultString, NumericalPokValue[].class)));
    }

    public ObjGroup getGroupTree(String isIncludedDel, String date, String typeTreeElements) throws Exception {
        return getGroupTree(isIncludedDel, date, typeTreeElements, 0);
    }

    //type
    //0 - группы для режима конструктора
    //1 - группы для режима оператора
    public ObjGroup getGroupTree(String isIncludedDel, String date, String typeTreeElements, Integer type) throws Exception {
        String res = createGetRequest(Main.reportRepoApiUrl + "objGroup/getGroupTree" +
                "?isIncludedDel=" + isIncludedDel + "&dateReport=" + date + "&typeTreeElements=" + typeTreeElements + "&type=" + type);
        return Main.objectMapper.readValue(res, ObjGroup.class);
    }

    public ArrayList<Form> getListByObjGroupId(String id, Boolean isIncludedDel) throws Exception {
        String res = createGetRequest(Main.reportRepoApiUrl + "form/getListByObjGroupId" +
                "?id=" + id + "&isIncludedDel=" + isIncludedDel);
        return new ArrayList<Form>(Arrays.asList(Main.objectMapper.readValue(res, Form[].class)));
    }

    public Form getFormById(String idForm) throws Exception {
        String res = createGetRequest(Main.reportRepoApiUrl + "form/getFormWithListFormMarks" +
                "?idForm=" + idForm);
        return Main.objectMapper.readValue(res, Form.class);
    }

    public byte[] getFileWithFormMarks(String idForm) throws Exception {
        HttpGet get = new HttpGet(Main.reportRepoApiUrl + "form/getFileWithFormMarks" +
                "?idForm=" + idForm);
        get.setHeader("Authorization", Main.token);
        //Конфигурация по умолчанию
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse result = httpClient.execute(get);

        if (result.getStatusLine().getStatusCode() != 200) {
            throw new Exception(EntityUtils.toString(result.getEntity(), StandardCharsets.UTF_8));
        }

        HttpEntity entity = result.getEntity();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        entity.writeTo(out);
        return out.toByteArray();
    }

    public void deleteForm(Form form) throws Exception {
        //Метод отправки
        HttpPost post = new HttpPost(Main.reportRepoApiUrl + "form/delete");
        post.setHeader("Authorization", Main.token);
        //Конфигурация по умолчанию
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //#4 Сбор параметров
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        builder.addTextBody("idForm", form.getId(), contentType);
        builder.addTextBody("reason", "Ручное удаление", contentType);

        HttpEntity entity = builder.build();
        post.setEntity(entity);
        CloseableHttpResponse result = httpClient.execute(post);
        System.out.println(result.getStatusLine().getStatusCode());

        String resultString = EntityUtils.toString(result.getEntity(), StandardCharsets.UTF_8);
        System.out.println(resultString);
        if (result.getStatusLine().getStatusCode() == 401) {
            throw new Exception("Ошибка авторизации. Заполните токен");
        }
        if (!(result.getStatusLine().getStatusCode() == 200)) {
            throw new Exception(resultString);
        }
    }
    //------------------------------------------------------------------------------------------------------------------
}
