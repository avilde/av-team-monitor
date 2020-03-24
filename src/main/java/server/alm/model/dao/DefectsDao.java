package server.alm.model.dao;

import server.alm.model.entity.QcDefect;

import java.util.List;

public interface DefectsDao {

    void saveDefect(QcDefect defect);

    int getDefectDbId(String project, String defectNum);

    List<QcDefect> getDefects(String sRetrieveType, String sTeamName, String sSortBy);

	List<QcDefect> getAllDefects();

    List<Object> getTeamProjects();

	int truncateTable(String table);

}
