package br.com.db.voting.utilities;


public class Utils {

	
	public static boolean isValidCpf(String cpf) {
        if (cpf == null || cpf.length() != 11 || !cpf.matches("\\d+")) {
            return false;
        }
        return true;
    }

}
