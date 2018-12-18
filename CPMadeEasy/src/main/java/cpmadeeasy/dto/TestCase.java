package cpmadeeasy.dto;

public class TestCase
{
	private String input, output;

	public String getInput()
	{
		return input;
	}

	public void setInput(String input)
	{
		this.input = input;
	}

	public String getOutput()
	{
		return output;
	}

	public void setOutput(String output)
	{
		this.output = output;
	}

	@Override public String toString()
	{
		return input + "=>\n" + output;
	}

	public TestCase(String input)
	{
		this.input = input;
	}

	public TestCase(String input, String output)
	{
		this.input = input;
		this.output = output;
	}

}
