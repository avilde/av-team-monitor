package global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import server.sec.SecurityUtils;
import server.team.model.dao.TeamsDao;
import server.team.model.entity.TmSetting;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@SuppressWarnings("ALL")
@Component
public class StartupContext {
	protected List<TmSetting> settings;
	@Autowired
	@Qualifier("TeamsDao")
	private TeamsDao teamsDao;

	@PostConstruct
	public void startup() throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
		settings = teamsDao.getServerSettings();
		SecurityUtils securityUtils = new SecurityUtils();

		String globalUser, globalPass;
		globalUser = getSetting("global_user");
		globalPass = getSetting("global_pass");

		if (globalUser == null || globalUser.equals(""))
			L.LOGGER.error("Application setting 'global_user' not set.");

		if (globalPass == null || globalPass.equals(""))
			L.LOGGER.error("Application setting 'global_pass' not set.");

		System.setProperty("tm.user", securityUtils.decrypt(globalUser));
		System.setProperty("tm.pass", securityUtils.decrypt(globalPass));
	}

	public String getSetting(String name) {
		String value = "";

		if (settings != null) {
			for (TmSetting setting : settings) {
				if (setting.getName().equals(name)) {
					value = setting.getValue();
					break;
				}
			}
		}

		return value;
	}
}
