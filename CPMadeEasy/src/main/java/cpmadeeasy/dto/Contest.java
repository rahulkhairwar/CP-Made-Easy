package cpmadeeasy.dto;

import com.cpmadeeasy.dto.enums.ContestType;
import com.cpmadeeasy.dto.enums.Phase;

/**
 * ..
 */
public class Contest
{
	private int id;
	private String name;
	private ContestType contestType;
	private Phase phase;
	private boolean frozen;
	private int durationSeconds, startTimeSeconds, relativeTimeSeconds, difficulty;
	private String preparedBy, websiteUrl, description, kind, icpcRegion, country, city, season;

	public Contest(int id, String name, ContestType contestType, Phase phase, boolean frozen, int durationSeconds,
				   int startTimeSeconds, int relativeTimeSeconds, int difficulty, String preparedBy, String websiteUrl,
				   String description, String kind, String icpcRegion, String country, String city, String season)
	{
		this.id = id;
		this.name = name;
		this.contestType = contestType;
		this.phase = phase;
		this.frozen = frozen;
		this.durationSeconds = durationSeconds;
		this.startTimeSeconds = startTimeSeconds;
		this.relativeTimeSeconds = relativeTimeSeconds;
		this.difficulty = difficulty;
		this.preparedBy = preparedBy;
		this.websiteUrl = websiteUrl;
		this.description = description;
		this.kind = kind;
		this.icpcRegion = icpcRegion;
		this.country = country;
		this.city = city;
		this.season = season;
	}

	public Contest()
	{
		// default, in case start time is not provided.
		startTimeSeconds = relativeTimeSeconds = difficulty = -1;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ContestType getContestType()
	{
		return contestType;
	}

	public void setContestType(String t)
	{
		switch (t)
		{
			case "CF":
				contestType = ContestType.CF;
				break;
			case "IOI":
				contestType = ContestType.IOI;
				break;
			case "ICPC":
				contestType = ContestType.ICPC;
				break;
			default:
				contestType = null;
				break;
		}
	}

	public Phase getPhase()
	{
		return phase;
	}

	public void setPhase(String p)
	{
		switch (p)
		{
			case "BEFORE":
				phase = Phase.BEFORE;
				break;
			case "CODING":
				phase = Phase.CODING;
				break;
			case "PENDING_SYSTEM_TEST":
				phase = Phase.PENDING_SYSTEM_TEST;
				break;
			case "SYSTEM_TEST":
				phase = Phase.SYSTEM_TEST;
				break;
			case "FINISHED":
				phase = Phase.FINISHED;
				break;
			default:
				phase = null;
				break;
		}
	}

	public boolean isFrozen()
	{
		return frozen;
	}

	public void setFrozen(boolean frozen)
	{
		this.frozen = frozen;
	}

	public int getDurationSeconds()
	{
		return durationSeconds;
	}

	public void setDurationSeconds(int durationSeconds)
	{
		this.durationSeconds = durationSeconds;
	}

	public int getStartTimeSeconds()
	{
		return startTimeSeconds;
	}

	public void setStartTimeSeconds(int startTimeSeconds)
	{
		this.startTimeSeconds = startTimeSeconds;
	}

	public int getRelativeTimeSeconds()
	{
		return relativeTimeSeconds;
	}

	public void setRelativeTimeSeconds(int relativeTimeSeconds)
	{
		this.relativeTimeSeconds = relativeTimeSeconds;
	}

	public int getDifficulty()
	{
		return difficulty;
	}

	public void setDifficulty(int difficulty)
	{
		this.difficulty = difficulty;
	}

	public String getPreparedBy()
	{
		return preparedBy;
	}

	public void setPreparedBy(String preparedBy)
	{
		this.preparedBy = preparedBy;
	}

	public String getWebsiteUrl()
	{
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl)
	{
		this.websiteUrl = websiteUrl;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getKind()
	{
		return kind;
	}

	public void setKind(String kind)
	{
		this.kind = kind;
	}

	public String getIcpcRegion()
	{
		return icpcRegion;
	}

	public void setIcpcRegion(String icpcRegion)
	{
		this.icpcRegion = icpcRegion;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{
		this.country = country;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getSeason()
	{
		return season;
	}

	public void setSeason(String season)
	{
		this.season = season;
	}

	@Override public String toString()
	{
		return String.format("id : %d, \tname : %s, \ttype : %s, \tphase : %s, \tfrozen : %c, \tduration(min) : %d, "
						+ "\tstartTime (sec) : %d, \trelativeTime (sec) : %d, \tdifficulty : %d, \tpreparedBy : %s, "
						+ "\twebsiteUrl : %s, \tdescription : %s, \tkind : %s, \ticpcRegion : %s, \tcountry : %s, "
						+ "\tcity : %s, \tseason : %s", id,
				name, contestType,
				phase, frozen ? 't' : 'f', durationSeconds / 60, startTimeSeconds, relativeTimeSeconds, difficulty,
				preparedBy, websiteUrl, description, kind, icpcRegion, country, city, season);
	}

	public Object[] getDisplayDataAsArray()
	{
		Object[] data = new Object[4];

		data[0] = id;
		data[1] = name;
		data[2] = contestType;
		data[3] = durationSeconds / 60;

		return data;
	}

}
