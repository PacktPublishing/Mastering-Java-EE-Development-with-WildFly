package it.vige.businesscomponents.persistence.converter;

import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PasswordConverter implements AttributeConverter<String, String> {

	public static String NEVER_DO_IT;
	
	@Override
	public String convertToDatabaseColumn(String password) {
		if (password != null) {
			String newPassword = getEncoder().encodeToString(password.getBytes());
			NEVER_DO_IT = newPassword;
			return newPassword;
		} else {
			return null;
		}
	}

	@Override
	public String convertToEntityAttribute(String password) {
		if (password != null) {
			return new String(getDecoder().decode(password));
		} else {
			return null;
		}
	}
}
