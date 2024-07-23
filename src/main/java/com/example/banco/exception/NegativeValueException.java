package com.example.banco.exception;

public class NegativeValueException extends BussinesException {

  public NegativeValueException() {
    super("Valor negativo não permitido.");
  }
}
