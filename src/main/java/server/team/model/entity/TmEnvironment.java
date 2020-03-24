package server.team.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "tm_environment", schema = "team_monitor", catalog = "")
public class TmEnvironment {
    private int rowId;
	private String envCode;
	private String envName;
	private String alternativeName;
	private String url;
	private String country;
	private String countryShort;
	private String keywords;

	@Id
	@Column(name = "row_id", nullable = false)
	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	@Basic
	@Column(name = "env_code", nullable = false, length = 10)
	public String getEnvCode() {
		return envCode;
	}

	public void setEnvCode(String envCode) {
		this.envCode = envCode;
	}

	@Basic
	@Column(name = "env_name", nullable = false, length = 100)
	public String getEnvName() {
		return envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

	@Basic
	@Column(name = "alternative_name", nullable = false, length = 100)
	public String getAlternativeName() {
		return alternativeName;
	}

	public void setAlternativeName(String alternativeName) {
		this.alternativeName = alternativeName;
	}

	@Basic
	@Column(name = "url", nullable = true, length = 1000)
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Basic
	@Column(name = "country", nullable = false, length = 45)
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Basic
	@Column(name = "country_short", nullable = true, length = 3)
	public String getCountryShort() {
		return countryShort;
	}

	public void setCountryShort(String countryShort) {
		this.countryShort = countryShort;
	}

	@Basic
	@Column(name = "keywords", nullable = true, length = 1000)
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

        TmEnvironment that = (TmEnvironment) o;

		if (rowId != that.rowId) return false;
		if (envCode != null ? !envCode.equals(that.envCode) : that.envCode != null) return false;
		if (envName != null ? !envName.equals(that.envName) : that.envName != null) return false;
		if (alternativeName != null ? !alternativeName.equals(that.alternativeName) : that.alternativeName != null)
			return false;
		if (url != null ? !url.equals(that.url) : that.url != null) return false;
		if (country != null ? !country.equals(that.country) : that.country != null) return false;
		if (countryShort != null ? !countryShort.equals(that.countryShort) : that.countryShort != null) return false;
		if (keywords != null ? !keywords.equals(that.keywords) : that.keywords != null);

		return true;
	}

	@Override
	public int hashCode() {
		int result = rowId;
		result = 31 * result + (envCode != null ? envCode.hashCode() : 0);
		result = 31 * result + (envName != null ? envName.hashCode() : 0);
		result = 31 * result + (alternativeName != null ? alternativeName.hashCode() : 0);
		result = 31 * result + (url != null ? url.hashCode() : 0);
		result = 31 * result + (country != null ? country.hashCode() : 0);
		result = 31 * result + (countryShort != null ? countryShort.hashCode() : 0);
		result = 31 * result + (keywords != null ? keywords.hashCode() : 0);
		return result;
	}
}
