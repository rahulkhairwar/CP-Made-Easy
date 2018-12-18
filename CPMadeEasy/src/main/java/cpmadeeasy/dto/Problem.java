package cpmadeeasy.dto;

import com.cpmadeeasy.dto.enums.ProblemType;

import java.util.List;

/**
 * ..
 */
public class Problem
{
	private int contestId;
	private String index, name;
	private ProblemType problemType;
	private double points;
	private List<String> tags;
	public TestCase[] tests;

	// set tests.

	public int getContestId()
	{
		return contestId;
	}

	public void setContestId(int contestId)
	{
		this.contestId = contestId;
	}

	public String getIndex()
	{
		return index;
	}

	public void setIndex(String index)
	{
		this.index = index;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ProblemType getProblemType()
	{
		return problemType;
	}

	public void setProblemType(String t)
	{
		switch (t)
		{
			case "PROGRAMMING":
				problemType = ProblemType.PROGRAMMING;
				break;
			case "QUESTION":
				problemType = ProblemType.QUESTION;
				break;
			default:
				problemType = null;
				break;
		}
	}

	public double getPoints()
	{
		return points;
	}

	public void setPoints(double points)
	{
		this.points = points;
	}

	public List<String> getTags()
	{
		return tags;
	}

	public void setTags(List<String> tags)
	{
		this.tags = tags;
	}

	@Override public String toString()
	{
		StringBuilder builder = new StringBuilder("[");
		int size = tags.size();

		for (int i = 0; i < size - 1; i++)
			builder.append(tags.get(i)).append(", ");

		if (tags.size() > 0)
			builder.append(tags.get(size - 1));

		builder.append("]");

		return String.format("contestId : %d, \tindex : %s, \tname : %s, \tproblemType : %s, \tpoints : %.2f",
				contestId, index, name, problemType, points) + ", tags : " + builder.toString();
	}

}
