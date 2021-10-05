package ru.rzd.discor.diskorReportConstReportBack.services;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import ru.rzd.discor.diskorReportConstReportBack.Main;
import ru.rzd.discor.diskorReportConstReportBack.models.core.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class CoreService {

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

    public ArrayList<AsSourceInformation> getListAsSourceInformation(Integer idPokValue) throws Exception {
        String result = createGetRequest(Main.coreApiUrl + "asSourceInformation/getAllByPokValue?id=" + idPokValue);
        ArrayList<AsSourceInformation> list = new ArrayList<AsSourceInformation>(Arrays.asList(Main.objectMapper.readValue(result, AsSourceInformation[].class)));
        list.removeIf(v -> !v.getCorTip().equals("I"));
        return list;
    }

    public ArrayList<CompAsToPokValue> getListCompAsToPokValue(Integer idPokValue, Integer idAsSourceInformation) throws Exception {
        String result = createGetRequest(Main.coreApiUrl + "compAsToPokValue/getByValuePokIdAndAsSourceInfId?idValuePok=" +
                idPokValue + "&idAsSourceInf=" + idAsSourceInformation);
        ArrayList<CompAsToPokValue> listCompAsToPokValue = new ArrayList<CompAsToPokValue>(Arrays.asList(Main.objectMapper.readValue(result, CompAsToPokValue[].class)));
        listCompAsToPokValue.removeIf(v -> !v.getCorTip().equals("I"));
        listCompAsToPokValue.sort((v1, v2) -> v2.getPriority() - v1.getPriority());
        return listCompAsToPokValue;
    }

    public ArrayList<GroupPok> getGroupPokTree() throws Exception {
        String result = createGetRequest(Main.coreApiUrl + "group/getAll");
        ArrayList<GroupPok> list = new ArrayList<GroupPok>(Arrays.asList(Main.objectMapper.readValue(result, GroupPok[].class)));
        list.removeIf(v -> !v.getCorTip().equals("I"));
        return list;
    }

    public ArrayList<GroupHandbook> getGroupHandbookTree() throws Exception {
        String result = createGetRequest(Main.coreApiUrl + "groupHandbook/getAll");
        ArrayList<GroupHandbook> list = new ArrayList<GroupHandbook>(Arrays.asList(Main.objectMapper.readValue(result, GroupHandbook[].class)));
        list.removeIf(v -> !v.getCorTip().equals("I"));
        return list;
    }

    public ArrayList<Handbook> getListHandbook(Integer idGroupHandbook) throws Exception {
        String result = createGetRequest(Main.coreApiUrl + "handbook/getByGroupHandbookId?id=" + idGroupHandbook);
        ArrayList<Handbook> list = new ArrayList<Handbook>(Arrays.asList(Main.objectMapper.readValue(result, Handbook[].class)));
        list.removeIf(v -> !v.getCorTip().equals("I"));
        return list;
    }

    public Handbook getHandbookById(Integer idHandbook) throws Exception {
        String result = createGetRequest(Main.coreApiUrl + "handbook/get?id=" + idHandbook);
        return Main.objectMapper.readValue(result, Handbook.class);
    }

    public ArrayList<HandbookInnerRecord> getListHandbookInnerRecord(Integer fkHandBook) throws Exception {
        String result = createGetRequest(Main.coreApiUrl + "handbook/getHandbookInnerRecordListByHandbookId?id=" + fkHandBook);
        return new ArrayList<HandbookInnerRecord>(Arrays.asList(Main.objectMapper.readValue(result, HandbookInnerRecord[].class)));
    }

    public ArrayList<Integer> getListHourReport() {
        ArrayList<Integer> listHourReport = new ArrayList<Integer>();
        for (Integer i = 0; i < 24; i++) {
            listHourReport.add(i);
        }
        return listHourReport;
    }

    public ArrayList<OffsetDate> getListOffsetDate() throws Exception {
        String result = createGetRequest(Main.coreApiUrl + "offset/getAll");
        ArrayList<OffsetDate> listOffsetDate = new ArrayList<OffsetDate>(Arrays.asList(Main.objectMapper.readValue(result, OffsetDate[].class)));
        listOffsetDate.removeIf(v -> !v.getCorTip().equals("I"));
        listOffsetDate.add(0, null);
        return listOffsetDate;
    }

    public ArrayList<ParamPok> getListParamPok(Integer idPok) throws Exception {
        String result = createGetRequest(Main.coreApiUrl + "params/getByPokId?id=" + idPok);
        ArrayList<ParamPok> list = new ArrayList<ParamPok>(Arrays.asList(Main.objectMapper.readValue(result, ParamPok[].class)));
        list.removeIf(v -> !v.getCorTip().equals("I"));
        return list;
    }

    public ArrayList<Pok> getListPok(Integer idGroupPok) throws Exception {
        String result = createGetRequest(Main.coreApiUrl + "pok/getByGroupId?groupId=" + idGroupPok);
        ArrayList<Pok> list = new ArrayList<Pok>(Arrays.asList(Main.objectMapper.readValue(result, Pok[].class)));
        list.removeIf(v -> !v.getCorTip().equals("I"));
        return list;
    }

    public Pok getPokById(Integer idPok) throws Exception {
        String result = createGetRequest(Main.coreApiUrl + "pok/get?id=" + idPok);
        return Main.objectMapper.readValue(result, Pok.class);
    }

    public ArrayList<PokValue> getListPokValue(Integer idPok) throws Exception {
        String result = createGetRequest(Main.coreApiUrl + "pokvalue/getByPokId?id=" + idPok);
        ArrayList<PokValue> list = new ArrayList<PokValue>(Arrays.asList(Main.objectMapper.readValue(result, PokValue[].class)));
        list.removeIf(v -> !v.getCorTip().equals("I"));
        return list;
    }
}

