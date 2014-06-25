package biz.sunce.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
 
public final class ResourceLoader
{
	 
	public InputStream getCsvInputStreamFromZip(String szip, String file)
	{
		InputStream is = null;

		try
		{
			ClassLoader classLoader = ResourceLoader.class.getClassLoader();
			URL resource = classLoader.getResource(szip);
			is = resource.openStream();
		}
		catch (IOException ex)
		{
			throw new IllegalArgumentException("File " + szip+ " not found");
		}

		try
		{
			ArchiveInputStream zip = new ArchiveStreamFactory().createArchiveInputStream("zip",is);
			ZipArchiveEntry entry = null;

			while ((entry = (ZipArchiveEntry) zip.getNextEntry()) != null)
			{
				if (entry.getName().equals(file))
				{
					return zip;
				}
			}
		}
		catch (ArchiveException e)
		{
			throw new IllegalStateException("Cannot unzip " + szip+ " E:"+e+" message:"+e.getMessage());
		}
		catch (IOException ex)
		{
			throw new IllegalStateException("Cannot read " + szip);
		}

		throw new IllegalStateException("Cannot find " + szip);
	}//getCsvInputStreamFromZip
 
}
