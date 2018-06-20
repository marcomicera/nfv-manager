package it.polito.dp2.NFV.sol3.service.gen;

import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaResteasyServerCodegen", date = "2018-06-18T15:27:35.051Z")
public class StringUtil {
	/**
	 * Check if the given array contains the given value (with case-insensitive
	 * comparison).
	 *
	 * @param array
	 *            The array
	 * @param value
	 *            The value to search
	 * @return true if the array contains the value
	 */
	public static boolean containsIgnoreCase(String[] array, String value) {
		for (String str : array) {
			if (value == null && str == null)
				return true;
			if (value != null && value.equalsIgnoreCase(str))
				return true;
		}
		return false;
	}

	/**
	 * Join an array of strings with the given separator.
	 * <p>
	 * Note: This might be replaced by utility method from commons-lang or guava
	 * someday if one of those libraries is added as dependency.
	 * </p>
	 *
	 * @param array
	 *            The array of strings
	 * @param separator
	 *            The separator
	 * @return the resulting string
	 */
	public static String join(String[] array, String separator) {
		int len = array.length;
		if (len == 0)
			return "";

		StringBuilder out = new StringBuilder();
		out.append(array[0]);
		for (int i = 1; i < len; i++) {
			out.append(separator).append(array[i]);
		}
		return out.toString();
	}

	/**
	 * Pretty-printing JAXB objects in an XML fashion.
	 * @param element	JAXB object to be pretty-printed.
	 * @return			the XML representation of the JAXB object.
	 */
	public static String toXml(JAXBElement<?> element) {
		try {
			JAXBContext jc = JAXBContext.newInstance(element.getValue().getClass());
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			marshaller.marshal(element, baos);
			return baos.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
