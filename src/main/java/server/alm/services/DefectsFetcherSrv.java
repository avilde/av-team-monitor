package server.alm.services;

import global.L;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import server.alm.model.dao.DefectsDao;
import server.alm.model.entity.QcDefect;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Objects.isNull;

@Transactional
public class DefectsFetcherSrv {
    // constants
    private static final String QC_PROJECT_SOL = "Solution_Maintenance";
    private static final String QC_PROJECT_SM = "Siebel_Maintenance";
    private static final String QC_DATE_FORMAT = "yyyy-MM-dd";
    private static final String QC_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // timing
    private long startTime;

    @Autowired
    @Qualifier("DefectsDao")
    @SuppressWarnings("unused")
    private DefectsDao defectsDAO;

    @SuppressWarnings("unused")
    @Scheduled(fixedRate = 300000)
    public void fetchDefects() throws IOException {
        @SuppressWarnings("unchecked") final List<QcDefect> defectList = new ArrayList();

        DefectsConnMgr defectsConnMgr = new DefectsConnMgr();
        JSONObject json;
        Boolean invalidResponse = false;
        Properties props = defectsConnMgr.props;

        // login in ALM
        try {
            startTime = System.nanoTime();

            defectsConnMgr.establishConnection();
            L.LOGGER.info("QC login.");

            List<Object> teamProjects = defectsDAO.getTeamProjects();
            for (Object teamProject : teamProjects) {
                Object[] projection = (Object[]) teamProject;
                // projection: 0-team alias, 1-project name, 2-project short name, 3-team column, 4-team name

                json = defectsConnMgr.getJsonResponse(createQcUrl(
                        props
                        , projection[1].toString()
                        , projection[0].toString()
                        , projection[3].toString()
                ));

                if (!isNull(json)) {

                    defectList.addAll(
                            parseJsonDefects(
                                    projection[1].toString()
                                    , projection[2].toString()
                                    , projection[4].toString()
                                    , projection[0].toString()
                                    , json
                            )
                    );
                } else {
                    invalidResponse = true;
                    break;
                }
            }

            if (!invalidResponse) {
                defectsDAO.truncateTable("qc_defect");
                L.LOGGER.info("Clearing defects table.");

                for (QcDefect defect : defectList) {
                    defectsDAO.saveDefect(defect);
                }
                L.LOGGER.info("Inserted " + defectList.size() + " defects.");
            } else
                L.LOGGER.error("One of responses had errors. Stopping defect update.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // logout from ALM
            defectsConnMgr.logOut();
            L.LOGGER.info("QC logout.");
        }

        L.LOGGER.info("Service execution time: " + ((System.nanoTime() - startTime) / 1000000) + " ms");
    }

    private List<QcDefect> parseJsonDefects(String sProject, String projectShortName, String sTeamName, String sTeamAlias, JSONObject jsonResponse) throws ParseException {
        @SuppressWarnings("unchecked")
        List<QcDefect> defects = new ArrayList();
        QcDefect defect;
        // each project has different fields used for the same information
        Iterator<?> responseKeys = jsonResponse.keys();
        String sRespKey;
        JSONArray jsonDefects;
        JSONArray jsonDefectFields;
        JSONArray jsonFieldValues;
        int iDefectKey;
        int iFieldKey;
        String sFieldName;
        String sFieldValue;

        while (responseKeys.hasNext()) {
            sRespKey = (String) responseKeys.next();
            if (jsonResponse.get(sRespKey) instanceof JSONArray && sRespKey.equals("entities")) {
                jsonDefects = jsonResponse.getJSONArray(sRespKey);
                for (iDefectKey = 0; iDefectKey < jsonDefects.length(); iDefectKey++) {
                    defect = new QcDefect();

                    jsonDefectFields = jsonDefects.getJSONObject(iDefectKey).getJSONArray("Fields"); // Fields
                    for (iFieldKey = 0; iFieldKey < jsonDefectFields.length(); iFieldKey++) {
                        sFieldName = jsonDefectFields.getJSONObject(iFieldKey).getString("Name");
                        jsonFieldValues = jsonDefectFields.getJSONObject(iFieldKey).getJSONArray("values");
                        sFieldValue = (jsonFieldValues.length() > 0
                                && jsonFieldValues.getJSONObject(0).has("value")
                                ? jsonFieldValues.getJSONObject(0).getString("value")
                                : "");

                        // each project has it's own fields
                        switch (sProject) {
                            case QC_PROJECT_SM:
                                switch (sFieldName) {
                                    case "user-08":
                                        defect.setTeam(sTeamName);
                                        break;
                                    case "user-18":
                                        defect.setAssignee(sFieldValue.toLowerCase());
                                        break;
                                    case "user-21":
                                        defect.setEnvironment(sFieldValue);
                                        break;
                                    case "user-35":
                                        defect.setTestArea(sFieldValue);
                                        break;
                                    case "user-12":
                                        defect.setReleaseNum(sFieldValue);
                                        break;
                                    case "user-23":
                                        defect.setHappyPathFlg((byte) (sFieldValue.equals("Y") ? 1 : 0));
                                        break;

                                }
                                break;
                            case QC_PROJECT_SOL:
                                switch (sFieldName) {
                                    case "user-18":
                                        defect.setTeam(sTeamName);
                                        break;
                                    case "owner":
                                        defect.setAssignee(sFieldValue.toLowerCase());
                                        break;
                                    case "user-22":
                                        defect.setEnvironment(sFieldValue);
                                        break;
                                    case "user-23":
                                        defect.setJiraItemId(sFieldValue);
                                        break;
                                    case "user-03":
                                        defect.setReleaseNum(sFieldValue);
                                        break;
                                    case "user-02":
                                        defect.setSystem(sFieldValue);
                                        break;
                                    case "user-20":
                                        defect.setCountry(sFieldValue);
                                        break;
                                }
                                break;
                        }
                        // common fields in all QC projects
                        switch (sFieldName) {
                            case "id":
                                if (defect.getItemKey() == null || defect.getItemKey().isEmpty())
                                    defect.setItemKey(projectShortName + '-' + sFieldValue);
                                break;
                            case "priority":
                                defect.setPriority(sFieldValue);
                                break;
                            case "severity":
                                defect.setSeverity(sFieldValue);
                                break;
                            case "description":
                                defect.setDescription(cleanText(sFieldValue));
                                break;
                            case "dev-comments":
                                defect.setDevComments(cleanText(sFieldValue));
                                break;
                            case "status":
                                defect.setStatus(sFieldValue);
                                break;
                            case "detected-by":
                                defect.setCreatedBy(sFieldValue);
                                break;
                            case "name":
                                defect.setSummary(sFieldValue.length() > 200 ? sFieldValue.substring(0, 200) : sFieldValue);
                                break;
                            case "last-modified":
                                defect.setUpdated(createTimestamp(sFieldValue, true));
                                break;
                            case "creation-time":
                                defect.setCreated(createTimestamp(sFieldValue, false));
                                break;
                            case "user-04":
                                defect.setCategory(sFieldValue);
                                break;
                            case "user-01":
                                defect.setBlockingFlg((byte) (sFieldValue.equals("Y") ? 1 : 0));
                                break;

                        }
                        defect.setProjectCd(projectShortName);
                        //global.L.LOGGER.info("Field: " + sFieldName + ", Value: " + sFieldValue);
                    }

                    defects.add(defect);
                }
            }
            // get defect count
            else if (sRespKey.equals("TotalResults")) {
                L.LOGGER.info("[project: " + sProject + " | team: " + sTeamName + " | team alias: " + sTeamAlias + "] defect count: " + jsonResponse.getInt(sRespKey));
            }
        }

        return defects;
    }

    private String createQcUrl(Properties props, String sProject, String sTeamAlias, String sTeamColumn) throws UnsupportedEncodingException {
        String url =         // http://t2qcprod2:8080/qcbin/rest
                "http" + (props.getProperty("secure").equals("Y") ? "s" : "") + "://"
                        + props.getProperty("host") + ":" + props.getProperty("port") + "/"
                        + props.getProperty("app") + "/" + "rest"
                        + "/domains/"
                        + props.getProperty("domain")
                        + "/projects/"
                        + sProject
                        + "/defects"
                        + props.getProperty("query") +
                        URLEncoder.encode("{" + sTeamColumn + "['" + sTeamAlias + "'];status[not('Closed')]" + "}", StandardCharsets.UTF_8.toString());
        // L.LOGGER.info(url);

        return url;
    }

    private Timestamp createTimestamp(String sDate, Boolean bTime) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat((bTime ? QC_DATETIME_FORMAT : QC_DATE_FORMAT));
        Date date = dateFormat.parse(sDate);
        //global.L.LOGGER.info("Parsing date: " + sDate);

        return new Timestamp(date.getTime());
    }

    private String cleanText(String sText) {
        sText = sText.replaceAll("&nbsp;", ""); // remove unnecessary empty lines
        sText = sText.replaceAll("ā€�|ā€¯", "\""); // replace QC diognal double quote

        return sText;
    }
}
