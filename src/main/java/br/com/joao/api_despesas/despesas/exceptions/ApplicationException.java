package br.com.joao.api_despesas.despesas.exceptions;

public class ApplicationException extends RuntimeException{

    public ApplicationException(String message) {
        super(message);
    }
}
