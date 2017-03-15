package ntu.ir.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {

	public static final String DOC_LOCATION = "DOC_LOCATION";
	public static final String INDEX_LOCATION_OF_MERGED_DOCUMENT = "INDEX_LOCATION_OF_MERGED_DOCUMENT";
	public static final String INDEX_LOCATION_OF_INDIVIDUAL_DOCUMENT = "INDEX_LOCATION_OF_INDIVIDUAL_DOCUMENT";
	public static final String ORIGINAL_DOC_LOCATION = "ORIGINAL_DOC_LOCATION";
	
	private static final Properties prop = new Properties();
	
	static
	{
		String propertyFile = "config.properties";
		try(InputStream propertyStream = ConfigLoader.class.getClassLoader().getResourceAsStream(propertyFile))
		{
			prop.load(propertyStream);
		} 
		catch (IOException e) 
		{
			throw new RuntimeException(e);
		}
	}
	
	public static String getConfig(String configName)
	{
		String config =  prop.getProperty(configName);
		return config;
	}
}
