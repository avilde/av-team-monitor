package server.alm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import server.alm.model.dao.DefectsDao;
import server.alm.model.entity.QcDefect;

import java.util.List;

@SuppressWarnings("unused")
@RestController
@CrossOrigin(origins = "http://localhost:9000")
public class DefectsController {
	@Autowired
	@Qualifier("DefectsDao")
	private DefectsDao defectsDao;

	private List<QcDefect> defects;

	@RequestMapping(value="getDefects", params = {"retrieve", "team", "sort"}, method = RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody
	List<QcDefect> getDefects(
			@RequestParam(value = "retrieve", required = false, defaultValue = "all") String sRetrieveType
			, @RequestParam(value = "team", required = false, defaultValue = "all") String sTeamName
			, @RequestParam(value = "sort", required = false, defaultValue = "severity") String sSortBy
	) {
		try {
			defects = defectsDao.getDefects(sRetrieveType, sTeamName, sSortBy);
		} catch (Exception e){
			e.printStackTrace();
		}

		return defects;
	}

	@RequestMapping(value = "getAllDefects", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody
	List<QcDefect> getAllDefects() {
		try {
			defects = defectsDao.getAllDefects();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return defects;
	}
}