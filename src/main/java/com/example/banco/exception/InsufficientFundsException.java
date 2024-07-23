package com.example.banco.exception;

public class InsufficientFundsException extends BussinesException {

  public InsufficientFundsException() {
    super("Saldo insuficiente.");
  }
}
